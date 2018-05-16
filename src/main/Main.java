package main;

import exception.InvalidParameterException;
import params.Params;
import world.DaisyWorld;

public class Main {

    // main method
    public static void main(String[] args) {
        int maxTick = 3000;
        int startPercentageOfWhites = 20;
        int startPercentageOfBlacks = 20;
        double albedoOfWhites = 0.75;
        double albedoOfBlacks = 0.25;
        double albedoOfSurface = 0.4;

        String scenario = Params.SCENARIO_RAMP_UP_RAMP_DOWN;

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
