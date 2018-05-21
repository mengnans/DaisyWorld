package main;

import exception.InvalidParameterException;
import params.Params;
import world.DaisyWorld;

/**
 * Mengnan Shi 802123
 * Mohammed Sharukh Syed 896508
 * @create 2018-04-30-15:07
 */


public class Main {

    // main method
    public static void main(String[] args) {
        // define the parameters
        // you can change here if you want
        int maxTick = 3000;
        int startPercentageOfWhites = 20;
        int startPercentageOfBlacks = 20;
        double albedoOfWhites = 0.75;
        double albedoOfBlacks = 0.25;
        double albedoOfSurface = 0.4;

        // you can switch to different scenario
//        String scenario = Params.SCENARIO_RAMP_UP_RAMP_DOWN;
//        String scenario = Params.SCENARIO_LOW_SOLAR_LUMINOSITY;
//        String scenario = Params.SCENARIO_HIGH_SOLAR_LUMINOSITY;
        String scenario = Params.SCENARIO_OUR_SOLAR_LUMINOSITY;
//        String scenario = Params.SCENARIO_WEATHER_EXPERIMENT;

        try {
            // Daisy World setup
            DaisyWorld.setup(
                    scenario,
                    startPercentageOfWhites,
                    startPercentageOfBlacks,
                    albedoOfWhites,
                    albedoOfBlacks,
                    albedoOfSurface);
        } catch (InvalidParameterException e) {
            // find invalid parameters
            e.printStackTrace();
        }

        //Daisy world go
        while(DaisyWorld.ticker.getTick() < maxTick){
            DaisyWorld.go();
        }

        // close the writer
        DaisyWorld.end();

    }
}
