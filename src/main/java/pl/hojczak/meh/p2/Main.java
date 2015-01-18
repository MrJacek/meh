/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import pl.hojczak.meh.utils.ThreadForParametersSet;
import pl.hojczak.meh.utils.PropertieName;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
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
            System.out.println("Start: "+LocalTime.now());
            
            
            if ("true".equals(prop.getProperty("all", "false"))) {

                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 3000));
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 4000));
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 5000));
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 6000));
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 7000));
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 8000));
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 9000));
                executeTh(prepareProperties(prop, PropertieName.PopulationSize, 10000));
                
                

                es.shutdown();
                es.awaitTermination(1, TimeUnit.DAYS);
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
        System.out.println("Stop :"+LocalTime.now());
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
