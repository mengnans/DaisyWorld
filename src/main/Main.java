package main;

import display.MyLineChart;
import params.Params;
import world.DaisyWorld;

public class Main {

    public static void main(String[] args) {
        double temperature = 22.5;
        double seedThreshold = ((0.1457 * temperature) - (0.0032 * (Math.pow(temperature,2))) - 0.6443);
        System.out.println(seedThreshold);

    }
}
