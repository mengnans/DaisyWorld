package world;

import entity.Daisy;
import entity.Location;
import entity.Patch;
import entity.Ticker;
import exception.InvalidParameterException;
import params.Params;

import java.util.Random;

/**
 * @author Mengnan Shi
 * @create 2018-05-03-10:45
 */

public class DaisyWorld {
    public static double solarLuminosity;
    private static double globaTemperature;
    public static String scenario;

    public static int startPercentageOfWhites;
    public static int startPercentageOfBlacks;

    public static double albedoOfWhites;
    public static double albedoOfBlacks;
    public static double albedoOfSurface;

    private double diffusion;

    private static Patch[][] patches;
    private static int patchNum;
    private static Ticker ticker;

    private static int whitesPopulation;
    private static int blacksPopulation;


    public static void setup(String scenario, int startPercentageOfWhites, int startPercentageOfBlacks,
                            double albedoOfWhites, double albedoOfBlacks, double albedoOfSurface){
        DaisyWorld.scenario = scenario;
        DaisyWorld.startPercentageOfBlacks = startPercentageOfBlacks;
        DaisyWorld.startPercentageOfWhites = startPercentageOfWhites;
        DaisyWorld.albedoOfWhites = albedoOfWhites;
        DaisyWorld.albedoOfBlacks = albedoOfBlacks;
        DaisyWorld.albedoOfSurface = albedoOfSurface;
        ticker = new Ticker();
        whitesPopulation = 0;
        blacksPopulation = 0;

//        try {
//            Params.checkParams();
//        } catch (InvalidParameterException e) {
//            e.printStackTrace();
//        }

        if(scenario.equals(Params.SCENARIO_OUR_SOLAR_LUMINOSITY)){
            solarLuminosity = 1.0;
        }
        if(scenario.equals(Params.SCENARIO_RAMP_UP_RAMP_DOWN)){
            solarLuminosity = 0.8;
        }
        if(scenario.equals(Params.SCENARIO_LOW_SOLAR_LUMINOSITY)){
            solarLuminosity = 0.6;
        }
        if(scenario.equals(Params.SCENARIO_HIGH_SOLAR_LUMINOSITY)){
            solarLuminosity = 1.4;
        }

        patches = new Patch[Params.WORLD_WIDTH][Params.WORLD_HEIGHT];
        for(int x = 0; x < Params.WORLD_WIDTH; x++){
            for(int  y = 0; y < Params.WORLD_WIDTH; y++){
                Patch patch = new Patch(new Location(x,y));
                patches[x][y] = patch;
            }
        }
        patchNum = Params.WORLD_WIDTH * Params.WORLD_HEIGHT;

        seedBlackRandomly();
        seedWhiteRandomly();
        calcTemperature(true);
    }

    private static void calcTemperature(boolean isFirstTime){
        // calc all the temperature
        double temperatureTemp = 0;
        for(int x = 0; x < Params.WORLD_WIDTH; x++){
            for(int  y = 0; y < Params.WORLD_WIDTH; y++){
                patches[x][y].calcTemperature();
                temperatureTemp += patches[x][y].getTemperature();
            }
        }
        // set the global temperature
        if(isFirstTime){
            globaTemperature = temperatureTemp / patchNum;
            System.out.println(globaTemperature);
        } else {
            // diffuse
        }

    }

    private static void seedBlackRandomly(){
        int blacksToSeed = patchNum * DaisyWorld.startPercentageOfBlacks / 100;
        Random random = new Random();
        while(blacksPopulation < blacksToSeed){
            int x = random.nextInt(Params.WORLD_WIDTH);
            int y = random.nextInt(Params.WORLD_HEIGHT);
            if(patches[x][y].noDaisyOnThisPatch()){
                Daisy blackDaisy = Daisy.getBlackDaisy(random.nextInt(Params.DAISY_MAX_AGE));
                patches[x][y].setDaisyOnThisPatch(blackDaisy);
                blacksPopulation ++;
            }
        }
    }

    private static void seedWhiteRandomly(){
        int whitesToSeed = patchNum * DaisyWorld.startPercentageOfWhites / 100;
        Random random = new Random();
        while(whitesPopulation < whitesToSeed){
            int x = random.nextInt(Params.WORLD_WIDTH);
            int y = random.nextInt(Params.WORLD_HEIGHT);
            if(patches[x][y].noDaisyOnThisPatch()){
                Daisy whiteDaisy = Daisy.getWhiteDaisy(random.nextInt(Params.DAISY_MAX_AGE));
                patches[x][y].setDaisyOnThisPatch(whiteDaisy);
                whitesPopulation ++;
            }
        }
    }


    public static void go(){



        if(scenario.equals(Params.SCENARIO_OUR_SOLAR_LUMINOSITY)){
            solarLuminosity = 1.0;
        }
        if(scenario.equals(Params.SCENARIO_RAMP_UP_RAMP_DOWN)){
            if(ticker.getTick() > 200 && ticker.getTick() <= 400)
                solarLuminosity += 0.005;
            if(ticker.getTick() > 600 && ticker.getTick() <= 850){
                solarLuminosity -= 0.0025;
            }
        }
        if(scenario.equals(Params.SCENARIO_LOW_SOLAR_LUMINOSITY)){
            solarLuminosity = 0.6;
        }
        if(scenario.equals(Params.SCENARIO_HIGH_SOLAR_LUMINOSITY)){
            solarLuminosity = 1.4;
        }

    }





}
