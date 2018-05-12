package world;

import entity.Daisy;
import entity.Location;
import entity.Patch;
import entity.Ticker;
import javafx.scene.chart.XYChart;
import params.Params;

import java.util.Random;

/**
 * @author Mengnan Shi
 * @create 2018-05-03-10:45
 */

public class DaisyWorld {
    public static double solarLuminosity;
    private static double globalTemperature;
    public static String scenario;

    public static int startPercentageOfWhites;
    public static int startPercentageOfBlacks;

    public static double albedoOfWhites;
    public static double albedoOfBlacks;
    public static double albedoOfSurface;

    private static double diffusion = 0.5;

    private static Patch[][] patches;
    private static int patchNum;
    public static Ticker ticker;

    public static int whitesPopulation;
    public static int blacksPopulation;

    public static XYChart.Series whitesPopulationSeries;
    public static XYChart.Series blacksPopulationSeries;
    public static XYChart.Series luminositySeries;
    public static XYChart.Series globalTermperatureSeries;

    private static void printInfo() {
        System.out.println("" + ticker.getTick() + "[" + whitesPopulation + ", " + blacksPopulation + ", " + globalTemperature + ", " + solarLuminosity + "]");

        whitesPopulationSeries.getData().add(new XYChart.Data(ticker.getTick(), whitesPopulation));
        blacksPopulationSeries.getData().add(new XYChart.Data(ticker.getTick(), blacksPopulation));
        globalTermperatureSeries.getData().add(new XYChart.Data(ticker.getTick(), globalTemperature));
    }

    public static void setup(String scenario, int startPercentageOfWhites, int startPercentageOfBlacks,
                             double albedoOfWhites, double albedoOfBlacks, double albedoOfSurface) {
        DaisyWorld.scenario = scenario;
        DaisyWorld.startPercentageOfBlacks = startPercentageOfBlacks;
        DaisyWorld.startPercentageOfWhites = startPercentageOfWhites;
        DaisyWorld.albedoOfWhites = albedoOfWhites;
        DaisyWorld.albedoOfBlacks = albedoOfBlacks;
        DaisyWorld.albedoOfSurface = albedoOfSurface;

        // TODO: check parameters are valid or not

        ticker = new Ticker();
        whitesPopulation = 0;
        blacksPopulation = 0;

        whitesPopulationSeries = new XYChart.Series();
        whitesPopulationSeries.setName("Whites Population");
        globalTermperatureSeries = new XYChart.Series();
        globalTermperatureSeries.setName("Global Temperature");
        blacksPopulationSeries = new XYChart.Series();
        blacksPopulationSeries.setName("Blacks Population");


        if (scenario.equals(Params.SCENARIO_OUR_SOLAR_LUMINOSITY)) {
            solarLuminosity = 1.0;
        }
        if (scenario.equals(Params.SCENARIO_RAMP_UP_RAMP_DOWN)) {
            solarLuminosity = 0.8;
        }
        if (scenario.equals(Params.SCENARIO_LOW_SOLAR_LUMINOSITY)) {
            solarLuminosity = 0.6;
        }
        if (scenario.equals(Params.SCENARIO_HIGH_SOLAR_LUMINOSITY)) {
            solarLuminosity = 1.4;
        }

        patches = new Patch[Params.WORLD_WIDTH][Params.WORLD_HEIGHT];
        for (int x = 0; x < Params.WORLD_WIDTH; x++) {
            for (int y = 0; y < Params.WORLD_WIDTH; y++) {
                Patch patch = new Patch(new Location(x, y));
                patches[x][y] = patch;
            }
        }
        patchNum = Params.WORLD_WIDTH * Params.WORLD_HEIGHT;

        seedBlackRandomly();
        seedWhiteRandomly();
        calcTemperature(true);

        printInfo();
    }

    private static void calcTemperature(boolean isSetupState) {
        // calc all the temperature
        double temperatureTemp = 0;
        for (int x = 0; x < Params.WORLD_WIDTH; x++) {
            for (int y = 0; y < Params.WORLD_WIDTH; y++) {
                patches[x][y].calcTemperature(diffusion);
                temperatureTemp += patches[x][y].getTemperature();
            }
        }
        // set the global temperature
        globalTemperature = temperatureTemp / patchNum;

        if (isSetupState) {
            for (int x = 0; x < Params.WORLD_WIDTH; x++) {
                for (int y = 0; y < Params.WORLD_WIDTH; y++) {
                    patches[x][y].resetTemperature();
                }
            }
        } else {

            // diffuse
            for (int x = 0; x < Params.WORLD_WIDTH; x++) {
                for (int y = 0; y < Params.WORLD_WIDTH; y++) {
                    patches[x][y].diffuse(patches);
                }
            }

            for (int x = 0; x < Params.WORLD_WIDTH; x++) {
                for (int y = 0; y < Params.WORLD_WIDTH; y++) {
                    patches[x][y].resetTemperature();
                }
            }

        }

    }

    private static void seedBlackRandomly() {
        int blacksToSeed = patchNum * DaisyWorld.startPercentageOfBlacks / 100;
        Random random = new Random();
        while (blacksPopulation < blacksToSeed) {
            int x = random.nextInt(Params.WORLD_WIDTH);
            int y = random.nextInt(Params.WORLD_HEIGHT);
            if (patches[x][y].noDaisyOnThisPatch()) {
                Daisy blackDaisy = Daisy.getBlackDaisy(random.nextInt(Params.DAISY_MAX_AGE));
                patches[x][y].setDaisyOnThisPatch(blackDaisy);
                blacksPopulation++;
            }
        }
    }

    private static void seedWhiteRandomly() {
        int whitesToSeed = patchNum * DaisyWorld.startPercentageOfWhites / 100;
        Random random = new Random();
        while (whitesPopulation < whitesToSeed) {
            int x = random.nextInt(Params.WORLD_WIDTH);
            int y = random.nextInt(Params.WORLD_HEIGHT);
            if (patches[x][y].noDaisyOnThisPatch()) {
                Daisy whiteDaisy = Daisy.getWhiteDaisy(random.nextInt(Params.DAISY_MAX_AGE));
                patches[x][y].setDaisyOnThisPatch(whiteDaisy);
                whitesPopulation++;
            }
        }
    }


    public static void checkSurviability() {
        for (int x = 0; x < Params.WORLD_WIDTH; x++) {
            for (int y = 0; y < Params.WORLD_WIDTH; y++) {
                if (!patches[x][y].noDaisyOnThisPatch()) {
                    patches[x][y].checkSurvivability(patches);
                }
            }
        }
    }


    public static void go() {

        calcTemperature(false);
        checkSurviability();
        ticker.tick();
        printInfo();

        // change luminosity based on different scenarios
        if (scenario.equals(Params.SCENARIO_OUR_SOLAR_LUMINOSITY)) {
            solarLuminosity = 1.0;
        }
        if (scenario.equals(Params.SCENARIO_RAMP_UP_RAMP_DOWN)) {
            if (ticker.getTick() > 200 && ticker.getTick() <= 400)
                solarLuminosity += 0.005;
            if (ticker.getTick() > 600 && ticker.getTick() <= 850) {
                solarLuminosity -= 0.0025;
            }
        }
        if (scenario.equals(Params.SCENARIO_LOW_SOLAR_LUMINOSITY)) {
            solarLuminosity = 0.6;
        }
        if (scenario.equals(Params.SCENARIO_HIGH_SOLAR_LUMINOSITY)) {
            solarLuminosity = 1.4;
        }

    }

}
