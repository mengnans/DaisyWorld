package entity;

import params.Params;
import world.DaisyWorld;

import java.util.ArrayList;
import java.util.Random;

/**
 * @create 2018-05-03-10:57
 */

public class Patch {

    private Location location;
    private Daisy daisyOnThisPatch;
    private final static Daisy NO_DAISY_ON_THIS_PATCH = null;

    private double temperature;
    private double temperatureLeft;
    private double diffusionLeft;
    private double diffusionReceived;


    public Patch(Location location) {
        this.location = location;
        daisyOnThisPatch = NO_DAISY_ON_THIS_PATCH;
        temperature = Params.INITIAL_PATCH_TEMPERATURE;
        temperatureLeft = 0;
        diffusionLeft = 0;
        diffusionReceived = 0;
    }

    public void setDaisyOnThisPatch(Daisy daisyOnThisPatch) {
        this.daisyOnThisPatch
                = daisyOnThisPatch;
    }

    public double getTemperature() {
        return temperature;
    }

    public Location getLocation() {
        return location;
    }

    public boolean noDaisyOnThisPatch() {
        return (this.daisyOnThisPatch
                == NO_DAISY_ON_THIS_PATCH);
    }

    public void calcTemperature(double diffusion) {
        double absorbedLuminosity;
        double localHeating;

        if (noDaisyOnThisPatch()) {
            absorbedLuminosity =
                    ((1 - DaisyWorld.albedoOfSurface)
                            * DaisyWorld.solarLuminosity);
        } else {
            absorbedLuminosity =
                    ((1 - this.daisyOnThisPatch.getAlbedo())
                            * DaisyWorld.solarLuminosity);
        }

        if (absorbedLuminosity > 0) {
            localHeating = 72 * Math.log(absorbedLuminosity) + 80;
        } else {
            localHeating = 80;
        }

        // calc new temp
        temperature = ((temperature + localHeating) / 2);

        // used for diffusion
        diffusionLeft = temperature * diffusion;
        temperatureLeft = temperature - diffusionLeft;
        diffusionReceived = 0;
    }


    public void diffuse(Patch[][] patches) {
        ArrayList<Location> neighbourLocations =
                patches[this.location.getX()][this.location.getY()]
                        .getLocation()
                        .getAllNeighbourLocations();

        // calc diffusion for each patch
        double diffusionToEachPatch = diffusionLeft / 8;

        for (Location neighbourLocation : neighbourLocations
                ) {
            int x = neighbourLocation.getX();
            int y = neighbourLocation.getY();
            // calc diffusion received for patch[x][y]
            patches[x][y].diffusionReceived += diffusionToEachPatch;
            // calc diffusion left for this patch
            diffusionLeft -= diffusionToEachPatch;

        }
    }

    public void resetTemperature() {
        temperature = temperatureLeft + diffusionLeft +diffusionReceived;
    }

    public void die() {
        // die
        if(Params.
                BLACK_COLOUR.equals(daisyOnThisPatch.getColour())){
            DaisyWorld.blacksPopulation --;
        } else {
            DaisyWorld.whitesPopulation --;
        }
        setDaisyOnThisPatch(NO_DAISY_ON_THIS_PATCH);
    }

    public void checkSurvivability(Patch[][] patches) {
        if(noDaisyOnThisPatch()){
            return;
        }

        double seedThreshold;

        daisyOnThisPatch.tick();
        if (daisyOnThisPatch.getAge()
                < daisyOnThisPatch.getMaxAge()) {
            seedThreshold = ((0.1457 * temperature) -
                    (0.0032 * (Math.pow(temperature,2))) - 0.6443);
            Random random = new Random();
            if (random.nextFloat() < seedThreshold) {
                ArrayList<Location> neighbourLocations =
                        location.getAllNeighbourLocations();
                // find random neighbour without daisy on it
                while (neighbourLocations.size() > 0) {
                    int randomIndex = random.nextInt(neighbourLocations.size());
                    Location neighbourLocation =
                            neighbourLocations.get(randomIndex);
                    neighbourLocations.remove(randomIndex);

                    int x = neighbourLocation.getX();
                    int y = neighbourLocation.getY();
                    // seed black daisy
                    if (patches[x][y].noDaisyOnThisPatch()) {
                        if (Params.
                                BLACK_COLOUR
                                .equals(daisyOnThisPatch.getColour())){
                            patches[x][y]
                                    .setDaisyOnThisPatch(
                                            Daisy.getBlackDaisy(
                                                    random.nextInt(
                                                            Params.DAISY_MAX_AGE
                                                    )
                                            )
                                    );
                            DaisyWorld.blacksPopulation ++;
                            break;
                        }
                        // seed white daisy
                        else {
                            patches[x][y]
                                    .setDaisyOnThisPatch(
                                            Daisy.getWhiteDaisy(
                                                    random.nextInt(
                                                            Params.DAISY_MAX_AGE
                                                    )
                                            )
                                    );
                            DaisyWorld.whitesPopulation ++;
                            break;
                        }
                    }

                }
            }
        } else {
            die();
        }

    }

}
