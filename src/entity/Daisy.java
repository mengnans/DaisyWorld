package entity;

import params.Params;

/**
 * @author Mengnan Shi
 * @create 2018-04-30-15:05
 */

public class Daisy {
    private double albedo;

    // how many ticks does this daisy live
    private int age;

    private Daisy(double albedo){
        this.albedo = albedo;
        this.age = 0;
    }

    public double getAlbedo() {
        return albedo;
    }

    public int getAge(){
        return this.age;
    }

    public static Daisy getBlackDaisy(){
        return new Daisy(Params.ALBEDO_OF_BLACKS);
    }

    public static Daisy getWhiteDaisy(){
        return new Daisy(Params.ALBEDO_OF_WHITES);
    }
}
