package world;

import entity.Daisy;
import entity.Location;
import entity.Patch;
import entity.Ticker;
import exception.InvalidParameterException;
import params.Params;
import untility.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Mengnan Shi
 * @create 2018-05-03-10:45
 */

public class DaisyWorld {
    public static double solarLuminosity;
    private static double globalTemperature;
    private static String scenario;

    private static int startPercentageOfWhites;
    private static int startPercentageOfBlacks;

    public static double albedoOfWhites;
    public static double albedoOfBlacks;
    public static double albedoOfSurface;

    private static double diffusion = 0.5;

    private static Patch[][] patches;
    private static int patchNum;
    public static Ticker ticker;

    public static int whitesPopulation;
    public static int blacksPopulation;

    private final static String FILE_NAME = "./data.csv";
    private static FileWriter fileWriter;

    // write data into csv file
    private static void writeData() {

//        print all data
//        System.out.println("" + ticker.getTick()
//                + "[" + whitesPopulation + ", "
//                + blacksPopulation + ", "
//                + globalTemperature + ", "
//                + solarLuminosity + "]");

        // store data in a Array List
        List<String> data = new ArrayList<>();
        data.add("" + ticker.getTick());
        data.add("" + whitesPopulation);
        data.add("" + blacksPopulation);
        data.add("" + globalTemperature);
        data.add("" + solarLuminosity);

        try {
            // write the data into csv file
            CSVWriter.writeLine(fileWriter, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setup(
            String scenario,
            int startPercentageOfWhites,
            int startPercentageOfBlacks,
            double albedoOfWhites,
            double albedoOfBlacks,
            double albedoOfSurface
    ) throws InvalidParameterException {

        // init the csv writer
        try {
            fileWriter = new FileWriter(FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write the header of csv
        List<String> data = new ArrayList<>();
        data.add("tick");
        data.add("whitesPopulation");
        data.add("blacksPopulation");
        data.add("globalTemperature");
        data.add("solarLuminosity");
        try {
            // write the data into csv file
            CSVWriter.writeLine(fileWriter, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // init the parameters
        DaisyWorld.scenario = scenario;
        DaisyWorld.startPercentageOfBlacks = startPercentageOfBlacks;
        DaisyWorld.startPercentageOfWhites = startPercentageOfWhites;
        DaisyWorld.albedoOfWhites = albedoOfWhites;
        DaisyWorld.albedoOfBlacks = albedoOfBlacks;
        DaisyWorld.albedoOfSurface = albedoOfSurface;

        // check parameters are valid or not
        checkStartPercentage(startPercentageOfBlacks);
        checkStartPercentage(startPercentageOfWhites);
        checkAlbedo(albedoOfBlacks);
        checkAlbedo(albedoOfWhites);
        checkAlbedo(albedoOfSurface);



        ticker = new Ticker();
        whitesPopulation = 0;
        blacksPopulation = 0;

        patches = new Patch[Params.WORLD_WIDTH][Params.WORLD_HEIGHT];
        for (int x = 0; x < Params.WORLD_WIDTH; x++) {
            for (int y = 0; y < Params.WORLD_HEIGHT; y++) {
                Patch patch = new Patch(new Location(x, y));
                patches[x][y] = patch;
            }
        }

        patchNum = Params.WORLD_WIDTH * Params.WORLD_HEIGHT;

        // randomly seed initial black and white daisies
        seedBlackRandomly();
        seedWhiteRandomly();


        // set init luminosity based on different scenarios
        switch (scenario) {
            case Params.SCENARIO_OUR_SOLAR_LUMINOSITY:
                solarLuminosity = 1.0;
                break;
            case Params.SCENARIO_RAMP_UP_RAMP_DOWN:
                solarLuminosity = 0.8;
                break;
            case Params.SCENARIO_LOW_SOLAR_LUMINOSITY:
                solarLuminosity = 0.6;
                break;
            case Params.SCENARIO_HIGH_SOLAR_LUMINOSITY:
                solarLuminosity = 1.4;
                break;
            case Params.SCENARIO_WEATHER_EXPERIMENT:
                applyWeather();
                break;
            default:
                solarLuminosity = 1.0;
                break;
        }


        calcTemperature(true);

        writeData();
    }

    // only used for weather experiment
    private static void applyWeather() {
        solarLuminosity = 1;
        // set initial weather
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;
        if (randomNumber <= 60) {
            // sunny
            solarLuminosity *= Params.SUNNY_LUMINOSITY_MULTIPLIER;
        } else if (randomNumber <= 80) {
            // cloudy
            solarLuminosity *= Params.CLOUDY_LUMINOSITY_MULTIPLIER;
        } else if (randomNumber <= 95) {
            // rainy
            solarLuminosity *= Params.RAINY_LUMINOSITY_MULTIPLIER;
        } else {
            // lightning
            solarLuminosity *= Params.LIGHTNING_LUMINOSITY_MULTIPLIER;
            // strike on a random position, and kill any daisy on that
            // location or near that location
            int randomX = random.nextInt(Params.WORLD_WIDTH);
            int randomY = random.nextInt(Params.WORLD_HEIGHT);
            Patch randomPatch = patches[randomX][randomY];

            // kill anything on that random patch
            if(!randomPatch.noDaisyOnThisPatch()){
                randomPatch.die();
            }

            // kill anything near that patch
            Location randomPatchLocation =
                    patches[randomX][randomY].getLocation();
            ArrayList<Location> allNeighbourLocations =
                    randomPatchLocation.getAllNeighbourLocations();
            for (Location neighbourLocation : allNeighbourLocations
                    ) {
                int neighbourX = neighbourLocation.getX();
                int neighbourY = neighbourLocation.getY();
                Patch neighbourPatch = patches[neighbourX][neighbourY];
                if(!neighbourPatch.noDaisyOnThisPatch()){
                    neighbourPatch.die();
                }
            }
        }
    }

    private static void checkAlbedo(double albedo) throws InvalidParameterException {
        if(albedo < 0 || albedo > 1){
            throw new InvalidParameterException("Invalid albedo parameter");
        }
    }

    private static void checkStartPercentage(double startPercentage) throws InvalidParameterException {
        if(startPercentage < 0 || startPercentage > 50){
            throw new InvalidParameterException("Invalid startPercentage parameter");
        }
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
                Daisy blackDaisy =
                        Daisy.getBlackDaisy(
                                random.nextInt(Params.DAISY_MAX_AGE));
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
                Daisy whiteDaisy = Daisy.getWhiteDaisy(
                        random.nextInt(Params.DAISY_MAX_AGE));
                patches[x][y].setDaisyOnThisPatch(whiteDaisy);
                whitesPopulation++;
            }
        }
    }


    private static void checkSurvivability() {
        for (int x = 0; x < Params.WORLD_WIDTH; x++) {
            for (int y = 0; y < Params.WORLD_WIDTH; y++) {
                if (!patches[x][y].noDaisyOnThisPatch()) {
                    patches[x][y].checkSurvivability(patches);
                }
            }
        }
    }

    // close the writer
    public static void end() {
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void go() {

        calcTemperature(false);
        checkSurvivability();
        ticker.tick();
        writeData();

        // change luminosity based on different scenarios
        switch (scenario) {
            case Params.SCENARIO_OUR_SOLAR_LUMINOSITY:
                solarLuminosity = 1.0;
                break;
            case Params.SCENARIO_RAMP_UP_RAMP_DOWN:
                if (ticker.getTick() > 200 && ticker.getTick() <= 400) {
                    solarLuminosity += 0.005;
                } else if (ticker.getTick() > 600 && ticker.getTick() <= 850) {
                    solarLuminosity -= 0.0025;
                }
                break;
            case Params.SCENARIO_LOW_SOLAR_LUMINOSITY:
                solarLuminosity = 0.6;
                break;
            case Params.SCENARIO_HIGH_SOLAR_LUMINOSITY:
                solarLuminosity = 1.4;
                break;
            case Params.SCENARIO_WEATHER_EXPERIMENT:
                if(ticker.getTick() % 100 == 0){
                    applyWeather();
                }
                break;
            default:
                solarLuminosity = 1.0;
                break;
        }

    }

}
