package entity;

import params.Params;
import world.DaisyWorld;

/**
 * @author Mengnan Shi
 * @create 2018-04-30-15:05
 */

public class Daisy {
    private double albedo;
    private String colour;

    // how many ticks does this daisy live
    private int age;
    private int maxAge;

    private Daisy(double albedo, String colour, int maxAge) {
        this.albedo = albedo;
        this.age = 0;
        this.maxAge = maxAge;
        this.colour = colour;
    }

    public double getAlbedo() {
        return albedo;
    }

    public String getColour() {
        return colour;
    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void tick(){ this.age ++; }

    public static Daisy getBlackDaisy(int maxAge) {
        return new Daisy(DaisyWorld.albedoOfBlacks, Params.BLACK_COLOUR, maxAge);
    }

    public static Daisy getWhiteDaisy(int maxAge) {
        return new Daisy(DaisyWorld.albedoOfWhites, Params.WHITE_COLOUR, maxAge);
    }


}
