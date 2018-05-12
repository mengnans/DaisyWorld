package display;

import entity.Daisy;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import params.Params;
import world.DaisyWorld;

/**
 * @author Mengnan Shi
 * @create 2018-05-12-22:38
 */

public class MyLineChart extends Application {

    private static final int MAX_TICK = 300;

    private void runDaisyWorld(){
        double albedoOfWhites = 0.75;
        double albedoOfBlacks = 0.25;
        double albedoOfSurface = 0.40;
        DaisyWorld.setup(Params.SCENARIO_OUR_SOLAR_LUMINOSITY,20,20,albedoOfWhites,albedoOfBlacks,albedoOfSurface);

        while(DaisyWorld.ticker.getTick() <= MAX_TICK){
            DaisyWorld.go();
        }
    }

    @Override
    public void start(Stage stage) {

        runDaisyWorld();

        NumberAxis tickAxis = new NumberAxis(0, MAX_TICK, 10);
        tickAxis.setLabel("Tick");

        NumberAxis populationAxis = new NumberAxis(0, 900, 50);
        populationAxis.setLabel("Population");

//        NumberAxis temperatureAxis = new NumberAxis(0, 50, 5);
//        populationAxis.setLabel("Temperature");

        LineChart linechart = new LineChart(tickAxis, populationAxis);

//        LineChart temperatureLineChart = new LineChart(tickAxis, temperatureAxis);


        linechart.getData().add(DaisyWorld.whitesPopulationSeries);
        linechart.getData().add(DaisyWorld.blacksPopulationSeries);

//        temperatureLineChart.getData().add(DaisyWorld.globalTermperatureSeries);

        //Creating a Group object
        Group root = new Group( linechart);

        //Creating a scene object
        Scene scene = new Scene(root, 1000, 800);

        //Setting title to the Stage
        stage.setTitle("Line Chart");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public static void main(String args[]){
        launch(args);
    }


}
