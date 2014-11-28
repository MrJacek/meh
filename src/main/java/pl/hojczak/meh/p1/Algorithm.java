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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import pl.hojczak.meh.p1.CoolingFactory.Cooling;
import pl.hojczak.meh.p1.CoolingFactory.Name;

/**
 *
 * @author jhojczak
 */
public class Algorithm {

    final int max;
    final int simulationCount;
    final double temperaturBegin;
    final double temperaturEnde;
    final double boltzmannsConstant;
    final private int maxIteration;
    int dimensions;
    final double mutationsRange;
    double temperatur;
    Properties properties;
    String resultFile;
    Random generator = new Random(new Date().getTime());
    List<SolutionsDual> extreamsCollection = new ArrayList<>();
    List<Solution> worstCollection = new ArrayList<>();
    List<Double> temps = new ArrayList<>();
    List<SolutionsDual> middleSolution = new ArrayList<>();
    Map<Double, List<Solution>> fullResults = new LinkedHashMap<>();

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(args[0]));
        Algorithm cont = new Algorithm(prop);
        cont.compute();
    }
    private final int dimensionsEnd;

    public Algorithm(Properties prop) {
        properties = prop;
        max = Integer.parseInt(prop.getProperty("max.iteration.per.temperature"));
        dimensions = Integer.parseInt(prop.getProperty("dimensions"));
        dimensionsEnd = Integer.parseInt(prop.getProperty("dimensions.end"));
        mutationsRange = Double.parseDouble(prop.getProperty("mutations.range"));
        resultFile = prop.getProperty("result.file");
        temperaturBegin = Double.parseDouble(prop.getProperty("temperature.start"));
        boltzmannsConstant = Double.parseDouble(prop.getProperty("k"));
        simulationCount = Integer.parseInt(prop.getProperty("simulation.count"));
        maxIteration = Integer.parseInt(prop.getProperty("max.iteration"));
        temperaturEnde = Double.parseDouble(prop.getProperty("temperature.end"));
    }

    public void init() {
        generator = new Random(new Date().getTime());
        extreamsCollection = new ArrayList<>();
        worstCollection = new ArrayList<>();
        temps = new ArrayList<>();
        middleSolution = new ArrayList<>();
        fullResults = new LinkedHashMap<>();
    }

    public void compute() throws IOException {
        do{
            System.out.println("For dimensions="+dimensions);
            System.out.println("Start simualtion");
            if ("true".equals(properties.getProperty("cooling.all"))) {

                for (Name coling : Name.values()) {
                    for (int i = 0; i < simulationCount; i++) {
                        SolutionsDual solution = simulate(CoolingFactory.create(coling, properties));

                        extreamsCollection.add(solution);
                    }
                    saveData(resultFile + "-" + coling.name());
                    System.out.println("Result was save in file:" + resultFile + "-" + coling.name());
                    init();
                }
                System.out.println("End simualtion");
            } else {

                for (int i = 0; i < simulationCount; i++) {
                    SolutionsDual solution = simulate(CoolingFactory.create(properties));
                    extreamsCollection.add(solution);
                }
                saveData(resultFile + "-" + properties.getProperty("coling"));
                System.out.println("End simualtion");
                System.out.println("Result was save in file:" + resultFile + "-" + properties.getProperty("coling"));
            }
        }while(dimensions++ != dimensionsEnd);

    }

    public SolutionsDual simulate(Cooling colling) {
        temperatur = temperaturBegin;
        Solution best = Solution.generate(dimensions, mutationsRange, new Random(new Date().getTime()));
        Solution worst = best;
        Solution current = best.copy();
        middleSolution = new ArrayList<>();
        temps = new ArrayList<>();
        int indexN = 0;
        do {
            int perTempIndex = 0;
            putSolution(temperatur, current);

            temps.add(temperatur);
            do {
                Solution fresh = current.mutates(generator);

                if (fresh.getFunValue() < current.getFunValue()) {
                    current = fresh.copy();

                    if (current.getFunValue() < best.getFunValue()) {
                        best = current.copy();
                    }
                    if (current.getFunValue() > worst.getFunValue()) {
                        worst = current.copy();
                    }
                } else {
                    if (acceptWorseSolution(current.getFunValue(), fresh.getFunValue())) {
                        current = fresh.copy();

                        if (current.getFunValue() < best.getFunValue()) {
                            best = current.copy();
                        }
                        if (current.getFunValue() > worst.getFunValue()) {
                            worst = current.copy();
                        }
                    }
                }
                perTempIndex++;
            } while (perTempIndex < max);
            temperatur = colling.next();
            indexN++;
        } while (indexN < maxIteration && temperatur > temperaturEnde);
        SolutionsDual result = new SolutionsDual();
        result.best = best;
        result.worst = worst;

        return result;
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

    public static class SolutionsDual {

        public Solution best;
        public Solution worst;
    }

    public void saveData(String filename) throws IOException {

        save(new File(filename + "-best.csv"), extreamsCollection);
        saveFull(new File(filename + ".csv"));
    }

    public void saveFull(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("id;");
            writer.append("temp;mean;\n");
            int id = 0;
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

    public void save(File file, List<SolutionsDual> collection, List<Double>... others) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("id;");
            for (int i = 0; i < dimensions; i++) {
                writer.append("best-x" + (i + 1) + ";");
            }
            writer.append("best-value;");
            for (int i = 0; i < dimensions; i++) {
                writer.append("worst-x" + (i + 1) + ";");
            }
            writer.append("worst-value;meanBest;standardVariationBest;meanWorst;standardVariationWorst\n");
            double averageBest = StatisticHelper.averageBest(collection);
            double standardVariationBest = StatisticHelper.standardVariationBest(collection, averageBest);
            double averageWorst = StatisticHelper.averageWorst(collection);
            double standardVariationWorst = StatisticHelper.standardVariationworst(collection, averageWorst);
            System.out.println(String.format("Best average:|%.3f| Worst average:|%.3f|", averageBest, averageWorst));
            for (int i = 0; i < collection.size(); i++) {
                SolutionsDual sol = collection.get(i);
                writer.append(i + ";" + sol.best.toString() + sol.worst.toString() + String.format("%.3f;%.3f;%.3f;%.3f", averageBest, standardVariationBest, averageWorst, standardVariationWorst));
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
