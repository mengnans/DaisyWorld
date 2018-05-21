package entity;

import params.Params;
import world.DaisyWorld;

/**
 * Mengnan Shi 802123
 * Mohammed Sharukh Syed 896508
 * @create 2018-04-30-15:05
 */

public class Daisy {
    private double albedo;
    private String colour;

    // how many ticks does this daisy live
    private int age;
    private int maxAge;

    private Daisy(){

    }

    private Daisy(double albedo, String colour, int maxAge) {
        this.albedo = albedo;
        this.age = 0;
        this.maxAge = maxAge;
        this.colour = colour;
    }

    double getAlbedo() {
        return albedo;
    }

    String getColour() {
        return colour;
    }

    int getAge() {
        return this.age;
    }

    int getMaxAge() {
        return maxAge;
    }

    void tick(){ this.age ++; }

    public static Daisy getBlackDaisy(int maxAge) {
        return new Daisy(
                DaisyWorld.albedoOfBlacks, Params.BLACK_COLOUR, maxAge);
    }

    public static Daisy getWhiteDaisy(int maxAge) {
        return new Daisy(
                DaisyWorld.albedoOfWhites, Params.WHITE_COLOUR, maxAge);
    }


}
