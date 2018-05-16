package params;


/**
 * @author Mengnan Shi
 * @create 2018-04-30-15:07
 */

public class Params {

    // colours of daisies
    public final static String WHITE_COLOUR = "White";
    public final static String BLACK_COLOUR = "Black";

    public final static double INITIAL_PATCH_TEMPERATURE = 0;

    public final static int DAISY_MAX_AGE = 25;

    // scenarios
    public final static String
            SCENARIO_RAMP_UP_RAMP_DOWN = "ramp up ramp down";
    public final static String
            SCENARIO_LOW_SOLAR_LUMINOSITY = "low solar luminosity";
    public final static String
            SCENARIO_OUR_SOLAR_LUMINOSITY = "our solar luminosity";
    public final static String
            SCENARIO_HIGH_SOLAR_LUMINOSITY = "high solar luminosity";

    // weather experiment scenario
    // each weather last for 100 ticks
    // 60% sunny: nothing happen
    // 20% cloudy: luminosity drops 10%
    // 15% rainy: luminosity drops 15%
    // 5% lightning: luminosity drops 15%
    // && kill a random daisy and its neighbours (once)
    public final static String
            SCENARIO_WEATHER_EXPERIMENT = "weather experiment";

    // following multipliers are used to calc the new luminosity
    public final static double SUNNY_LUMINOSITY_MULTIPLIER = 1.0;
    public final static double CLOUDY_LUMINOSITY_MULTIPLIER = 0.9;
    public final static double RAINY_LUMINOSITY_MULTIPLIER = 0.85;
    public final static double LIGHTNING_LUMINOSITY_MULTIPLIER = 0.85;

    // the size of the daisy world
    public final static int WORLD_WIDTH = 29;
    public final static int WORLD_HEIGHT = 29;

}
