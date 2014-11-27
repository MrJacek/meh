/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import pl.hojczak.meh.p1.CoolingFactory.Cooling;

/**
 *
 * @author jhojczak
 */
public class Algorithm {

    int max;
    double boltzmannsConstant;
    double temperatur;
    int simulationCount;
    double temperaturEnde;
    Properties properties;

    int dimensions;
    double mutationsRange;
    String resultFile;
    private final double temperaturBegin;
    Random generator = new Random(new Date().getTime());
    List<Solution> bestCollection = new ArrayList<>();
    List<Solution> worstCollection = new ArrayList<>();
    List<Double> temps = new ArrayList<>();
    List<Solution> middleSolution = new ArrayList<>();
    Map<Double, List<Solution>> fullResults = new LinkedHashMap<>();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(args[0]));
        Algorithm cont = new Algorithm(prop);
        cont.compute();
        cont.saveData();

    }

    public Algorithm(Properties prop) {
        properties = prop;
        max = Integer.parseInt(prop.getProperty("max.iteration.per.temperature"));
        dimensions = Integer.parseInt(prop.getProperty("dimensions"));
        mutationsRange = Double.parseDouble(prop.getProperty("mutations.range"));
        resultFile = prop.getProperty("result.file");
        temperaturBegin = Double.parseDouble(prop.getProperty("temperature.start"));
        boltzmannsConstant = Double.parseDouble(prop.getProperty("k"));
        simulationCount = Integer.parseInt(prop.getProperty("simulation.count"));
        temperaturEnde = Double.parseDouble(prop.getProperty("temperature.end"));

    }

    public void compute() throws IOException {

        System.out.println("Start simualtion");

        for (int i = 0; i < simulationCount; i++) {
            Solution solution = simulate(CoolingFactory.create(properties));
            System.out.println("First iteration: tmpsCount=" + fullResults.size());
            save(new File(resultFile + "-middle-" + i + ".csv"), middleSolution, temps);
            bestCollection.add(solution);
        }
        System.out.println("End simualtion");
        System.out.println("Result was save in file:" + resultFile);

    }

    public Solution simulate(Cooling colling) {
        temperatur = temperaturBegin;
        Solution best = Solution.generate(dimensions, mutationsRange, new Random(new Date().getTime()));
        Solution worst = best;
        Solution current = best.copy();
        middleSolution = new ArrayList<>();
        temps = new ArrayList<>();

        do {
            int index = 0;
            putSolution(temperatur, current);
            middleSolution.add(current);
            temps.add(temperatur);
            do {
                Solution fresh = current.mutates(generator);
     
                if (fresh.getFunValue() < current.getFunValue()) {
                    current = fresh.copy();

                    best = getBestSolution(current, best);
                    worst = getWorstSolution(current, worst);
                } else {
                    if (acceptWorseSolution(current.getFunValue(), fresh.getFunValue())) {
                        current = fresh.copy();

                        best = getBestSolution(current, best);
                        worst = getWorstSolution(current, worst);
                    }
                }
                index = index + 1;
            } while (index < max);
            temperatur = colling.next();
        } while (temperatur > temperaturEnde);
        return best;
    }

    private Solution getWorstSolution(Solution current, Solution worst) {
        if (current.getFunValue() > worst.getFunValue()) {
            worst = current.copy();
        }
        return worst;
    }

    private Solution getBestSolution(Solution current, Solution best) {
        if (current.getFunValue() < best.getFunValue()) {
            best = current.copy();
        }
        return best;
    }

    private boolean acceptWorseSolution(double worseSolution, double betterSolution) {

        double y = generator.nextDouble();
        double tempWithBoltzman = (boltzmannsConstant * temperatur);
        double tmp = -1d * (worseSolution - betterSolution) / tempWithBoltzman;
        double chance;
        if (tmp < 1d) {
            chance = Math.expm1(tmp);
        } else {
            chance = Math.exp(tmp);
        }
        return y < chance;

    }

    private void putSolution(double temp, Solution current) {
        List<Solution> t = fullResults.get(temp);
        if (t == null) {
            t = new ArrayList<>();
        }
        t.add(current);
        fullResults.put(temp, t);

    }

    public void saveData() throws IOException {
        save(new File(resultFile + "-best.csv"), bestCollection);
        saveFull(new File(resultFile + ".csv"));
    }

    public void saveFull(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("id;");
            writer.append("temp;mean;\n");
            int id = 0;
            System.out.println("Size full statistic:" + fullResults.entrySet().size());
            for (Map.Entry<Double, List<Solution>> entrySet : fullResults.entrySet()) {

                Double temp = entrySet.getKey();
                List<Solution> values = entrySet.getValue();
                double average = StatisticHelper.average(values);
                writer.append(String.format("%d;%.3f;%.3f", id, temp, average) + "\n");
                ++id;
            }
            writer.flush();
        }
    }

    public void save(File file, List<Solution> collection, List<Double>... others) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("id;");
            for (int i = 0; i < dimensions; i++) {
                writer.append("x" + (i + 1) + ";");
            }
            writer.append("value;mean;standardVariation\n");
            double average = StatisticHelper.average(collection);
            double standardVariation = StatisticHelper.standardVariation(collection, average);

            for (int i = 0; i < collection.size(); i++) {

                writer.append(i + ";" + collection.get(i).toString() + String.format("%.3f;%.3f", average, standardVariation));
                if (others.length > 0) {
                    for (List<Double> o : others) {
                        writer.append(String.format(";%.3f", o.get(i)));
                    }
                }
                writer.append("\n");
            }
            writer.flush();
        }
    }

}
