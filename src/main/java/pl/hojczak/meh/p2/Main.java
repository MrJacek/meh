/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhojczak
 */
public class Main {

    public static void main(String[] args) {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(args[0]));
            Problem problem = Problem.createProblemFromFile(prop.getProperty("problem.file", ""));
            System.out.println("Availbe processors: " + Runtime.getRuntime().availableProcessors());

            if ("true".equals(prop.getProperty("all", "false"))) {
                ExecutorService executorService = Executors.newWorkStealingPool();

            } else {
                Algorithm algorithm = new Algorithm(prop, problem, new Random(new Date().getTime()));
                algorithm.compute();
                algorithm.finish();
                System.out.println("BEST  =  " + algorithm.getBest());
                System.out.println("WORST =  " + algorithm.getWorst());
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class OneSetOfParametersThread implements Runnable {

        Properties prop;
        Problem problem;
        int[] meansOfMeans;
        int[] meansOfBests;
        int[] meansOfWorst;
        List<Individual> best;
        List<Individual> worst;
        Helper h = new Helper();

        public OneSetOfParametersThread(Properties prop, Problem problem) {
            this.prop = prop;
            this.problem = problem;
            meansOfBests = new int[h.getIntegerProp("max.iteration", prop)];

        }

        @Override
        public void run() {
            String fileName = prop.getProperty("output.file");
            for (int i = 0; i < ITERATION_PER_PARAMETERS; i++) {
                prop.setProperty("output.file", fileName + "." + i);
                try {
                    Algorithm algorithm = new Algorithm(prop, problem, new Random(new Date().getTime()));
                    algorithm.compute();
                    algorithm.finish();
                    for (int j = 0; j < meansOfBests.length; j++) {
                        meansOfMeans[j] += algorithm.getMeans()[j];
                    }
                    best.add(algorithm.getBest());
                    worst.add(algorithm.getWorst());
                } catch (IOException ex) {
                    throw new IllegalStateException(ex);
                }
            }
            for (int i = 0; i < meansOfMeans.length; i++) {
                meansOfMeans[i] = meansOfMeans[i] / ITERATION_PER_PARAMETERS;
            }
        }
        private static final int ITERATION_PER_PARAMETERS = 10;

    }
}
