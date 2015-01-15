/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhojczak
 */
public class Main {

    public static ExecutorService es = Executors.newWorkStealingPool();
    public static Problem problem;
    public static List<ThreadForParametersSet> threads = new ArrayList<>();
    public static int MAX_ITERATIONS;

    public static void main(String[] args) throws InterruptedException {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(args[0]));
            problem = Problem.createProblemFromFile(prop.getProperty(PropertieName.ProblemFile.value, ""));
            MAX_ITERATIONS = Integer.parseInt(prop.getProperty(PropertieName.Iteration.value));
            System.out.println("Availbe processors: " + Runtime.getRuntime().availableProcessors());
            System.out.println(prop.toString());
            if ("true".equals(prop.getProperty("all", "false"))) {
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 100));
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 700));
//                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 1000));
//                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 1300));
//                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 1700));
//                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 2000));
//                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 3000));

                executeTh(prepareProperties(prop, PropertieName.MutateChance, 5));
                executeTh(prepareProperties(prop, PropertieName.MutateChance, 10));
//                executeTh(prepareProperties(prop, PropertieName.MutateChance, 15));
//                executeTh(prepareProperties(prop, PropertieName.MutateChance, 25));
//                executeTh(prepareProperties(prop, PropertieName.MutateChance, 30));
//                executeTh(prepareProperties(prop, PropertieName.MutateChance, 35));
//                executeTh(prepareProperties(prop, PropertieName.MutateChance, 40));
//                executeTh(prepareProperties(prop, PropertieName.MutateChance, 45));
//                executeTh(prepareProperties(prop, PropertieName.MutateChance, 50));

                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 99));
                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 95));
//                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 90));
//                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 85));
//                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 80));
//                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 70));
//                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 65));
//                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 60));
//                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 55));
//                executeTh(prepareProperties(prop, PropertieName.IntercroosingChance, 50));

                es.shutdown();
                es.awaitTermination(1, TimeUnit.HOURS);
                saveMeans();
            } else {
                Algorithm algorithm = new Algorithm(prop, problem, new Random(new Date().getTime()), false);
                algorithm.compute();
                algorithm.finish();
                System.out.println("Best: " + algorithm.getBest());
                System.out.println("Worst: " + algorithm.getWorst());
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void executeTh(Properties prop) {
        ThreadForParametersSet th = new ThreadForParametersSet(problem, prop);
        threads.add(th);
        es.submit(th);
    }

    private static Properties prepareProperties(Properties prop, PropertieName name, int value) {
        Properties result = copyProperties(prop);
        result.setProperty(name.value, "" + value);
        String outputFile = name.name() + "-" + value;
        result.setProperty(PropertieName.OutputFile.value, outputFile);
        return result;
    }

    public static Properties copyProperties(Properties prop) {
        Properties result = new Properties();
        for (Object k : prop.keySet()) {
            result.setProperty(k.toString(), prop.getProperty(k.toString()));
        }
        return result;
    }

    private static void saveMeans() throws IOException {
        try (FileWriter w = new FileWriter("means.csv")) {
            for (ThreadForParametersSet thread : threads) {
                w.append(thread.fileName).append(";");
            }
            w.append("\n");
            for (int i = 0; i < MAX_ITERATIONS; i++) {
                for (ThreadForParametersSet thread : threads) {
                    w.append(Long.toString(thread.meansOfMeans[i])).append(";");
                }
                w.append("\n");
            }
            w.flush();
        }
        try (FileWriter w = new FileWriter("summary.csv")) {
            w.append("#;");
            for (ThreadForParametersSet thread : threads) {
                w.append(thread.fileName).append(";");
            }
            w.append("\n").append("meanTime");
            for (ThreadForParametersSet thread : threads) {
                w.append(";" + thread.meanTime);
            }
            w.append("\n").append("meansOfBests");
            for (ThreadForParametersSet thread : threads) {
                w.append(";" + thread.meansOfBests);
            }
            w.append("\n").append("meansOfWorst");
            for (ThreadForParametersSet thread : threads) {
                w.append(";" + thread.meansOfWorst);
            }
            w.append("\n").append("best");
            for (ThreadForParametersSet thread : threads) {
                w.append(";\"" + thread.best + "\"");
            }
            w.append("\n").append("worst");
            for (ThreadForParametersSet thread : threads) {
                w.append(";\"" + thread.worst + "\"");
            }
            w.flush();
        }
    }
}
