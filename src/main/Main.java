package main;

import entity.Daisy;
import params.Params;
import world.DaisyWorld;

public class Main {


    public static void main(String[] args) {
        int maxTick = 3000;

        double albedoOfWhites = 0.75;
        double albedoOfBlacks = 0.25;
        double albedoOfSurface = 0.40;
        DaisyWorld.setup(Params.SCENARIO_OUR_SOLAR_LUMINOSITY,20,20,albedoOfWhites,albedoOfBlacks,albedoOfSurface);

        while(DaisyWorld.ticker.getTick() <= maxTick){
            DaisyWorld.go();
        }

        // close the writer
        DaisyWorld.end();

    }
}
