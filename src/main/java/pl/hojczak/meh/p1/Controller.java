/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import pl.hojczak.meh.p1.CoolingFactory.Cooling;

/**
 *
 * @author jhojczak
 */
public class Controller {

    Random generator = new Random(new Date().getTime());
    int max = 10;
    double boltzmannsConstant;
    double temperatur;
    Cooling cooling;
    List<Solution> bestCollection = new ArrayList<>();
    List<Double> temps = new ArrayList<>();
    int simulationCount;
    double temperaturEnde;

    int dimensions;
    double mutationsRange;
    String resultFile;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(args[0]));
        Controller cont = new Controller(prop);
        cont.compute();
        cont.saveData();

    }

    public Controller(Properties prop) {
        max = Integer.parseInt(prop.getProperty("max.iteration.per.temperature"));
        cooling = CoolingFactory.create(prop);
        dimensions = Integer.parseInt(prop.getProperty("dimensions"));
        mutationsRange = Double.parseDouble(prop.getProperty("mutations.range"));
        resultFile = prop.getProperty("result.file");
        temperatur = Double.parseDouble(prop.getProperty("temperature.start"));
        boltzmannsConstant = Double.parseDouble(prop.getProperty("k"));
        simulationCount = Integer.parseInt(prop.getProperty("simulation.count"));
        temperaturEnde = Double.parseDouble(prop.getProperty("temperature.end"));

    }

    public void saveData() throws IOException {
        try (FileWriter writer = new FileWriter(resultFile)) {
            writer.append("id;");
            for (int i = 0; i < dimensions; i++) {
                writer.append("x" + (i + 1) + ";");
            }
            writer.append("value;mean;standardVariation\n");
            double average = StatisticHelper.average(bestCollection);
            double standardVariation = StatisticHelper.standardVariation(bestCollection, average);

            for (int i = 0; i < bestCollection.size(); i++) {

                writer.append(i + ";" + bestCollection.get(i).toString() + String.format("%.3f;%.3f", average, standardVariation) + "\n");
            }
            writer.flush();
        }
    }

    public void saveParameters() throws IOException {
        try (FileWriter writer = new FileWriter(resultFile)) {
            writer.append("id;");
            for (int i = 0; i < dimensions; i++) {
                writer.append("x" + (i + 1) + ";");
            }
            writer.append("value;mean;standardVariation\n");
            double average = StatisticHelper.average(bestCollection);
            double standardVariation = StatisticHelper.standardVariation(bestCollection, average);

            for (int i = 0; i < bestCollection.size(); i++) {

                writer.append(i + ";" + bestCollection.get(i).toString() + String.format("%.3f;%.3f", average, standardVariation) + "\n");
            }
            writer.flush();
        }
    }

    public void compute() {

        System.out.println("Start simualtion");

        for (int i = 0; i < simulationCount; i++) {
            LocalTime start = LocalTime.now();
            Solution solution = simulate();
            LocalTime end = LocalTime.now();
            System.out.println("Estymaite end time for:" + end.minusNanos(start.getNano()).toString());
            bestCollection.add(solution);
        }
        System.out.println("End simualtion");
        System.out.println("Result was save in file:" + resultFile);

    }

    public Solution simulate() {
        Solution best = Solution.generate(dimensions, mutationsRange, new Random(new Date().getTime()));
        Solution current = best.copy();

        do {
            int index = 0;
//            System.out.println("["+temperatur+"] :");
            do {
//                System.out.print(index);
                Solution fresh = current.mutates();
                if (fresh.getFunValue() < current.getFunValue()) {
                    current = fresh.copy();
                    if (current.getFunValue() < best.getFunValue()) {
                        best = current.copy();
                    }
                } else {
                    if (acceptWorseSolution(current.getFunValue(), fresh.getFunValue())) {
                        current = fresh.copy();
                        if (current.getFunValue() < best.getFunValue()) {
                            best = current.copy();
                        }
                    }
                }
                index = index + 1;
            } while (index < max);
            temperatur = cooling.next();
            temps.add(temperatur);
        } while (temperatur > temperaturEnde);
        return best;
    }

    private boolean acceptWorseSolution(double worseSolution, double betterSolution) {

        double y = generator.nextDouble();
        double tempWithBoltzman = (boltzmannsConstant * temperatur);
        double tmp = -1d * (worseSolution - betterSolution) / tempWithBoltzman;
        double chance = Math.expm1(tmp);
//        System.out.println("acceptWorseSolution: "+y+" < "+ chance+" || "+tmp+" = ["+-1d * (worseSolution - betterSolution)+" / "+tempWithBoltzman+"]");
        return y < chance;

    }

}
