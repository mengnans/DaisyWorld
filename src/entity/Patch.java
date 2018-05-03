package entity;

import params.Params;
import world.DaisyWorld;

/**
 * @author Mengnan Shi
 * @create 2018-05-03-10:57
 */

public class Patch {

    private Location location;
    private Daisy daisyOnThisPatch;
    private final static Daisy NO_DAISY_ON_THIS_PATCH = null;
    private double temperature;

    public Patch(Location location){
        this.location = location;
        daisyOnThisPatch = NO_DAISY_ON_THIS_PATCH;
        temperature = Params.INITIAL_PATCH_TEMPERATURE;
    }

    public void setDaisyOnThisPatch(Daisy daisyOnThisPatch) {
        this.daisyOnThisPatch = daisyOnThisPatch;
    }

    public double getTemperature() {
        return temperature;
    }

    public Location getLocation() {
        return location;
    }

    public boolean noDaisyOnThisPatch(){
        return (this.daisyOnThisPatch == NO_DAISY_ON_THIS_PATCH);
    }

    public void calcTemperature(){
        double abosorbedLuminosity;
        double localHeating;

        if(noDaisyOnThisPatch()){
            abosorbedLuminosity = ((1- DaisyWorld.albedoOfSurface) * DaisyWorld.solarLuminosity);
        } else {
            abosorbedLuminosity = ((1- this.daisyOnThisPatch.getAlbedo()) * DaisyWorld.solarLuminosity);
        }

        if(abosorbedLuminosity > 0){
            localHeating = 72 * Math.log(abosorbedLuminosity) + 80;
        } else {
            localHeating = 80;
        }

        this.temperature = ((this.temperature + localHeating) / 2);
    }


}
