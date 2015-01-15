/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhojczak
 */
public class ThreadForParametersSet implements Runnable {

    final int ITERATION_PER_PARAMETERS;
    Properties prop;
    Problem problem;
    long[] meansOfMeans;
    long meansOfBests = 0;
    long meansOfWorst = 0;
    List<Individual> best;
    List<Individual> worst;
    long meanTime;
    Helper h = new Helper();
    String fileName;

    public ThreadForParametersSet(Problem problem, Properties prop) {
        this.prop = prop;
        this.problem = problem;
        meansOfMeans = new long[h.getIntegerProp(PropertieName.Iteration.value, this.prop)];
        ITERATION_PER_PARAMETERS = h.getIntegerProp(PropertieName.IterationPerParam.value, this.prop);
        best = new ArrayList<>();
        worst = new ArrayList<>();
    }
   

    @Override
    public void run() {
        fileName = this.prop.getProperty(PropertieName.OutputFile.value);

        for (int i = 0; i < ITERATION_PER_PARAMETERS; i++) {
            this.prop.setProperty(PropertieName.OutputFile.value, fileName + "-" + i);
            System.out.println("Start: " + fileName + " :" + i);
            try {
                Algorithm algorithm = new Algorithm(this.prop, problem, new Random(new Date().getTime()), true);
                LocalTime start = LocalTime.now();
                // Execute simulation
                algorithm.compute();

                LocalTime end = LocalTime.now();
                Duration duration = Duration.between(start, end);
                meanTime += duration.getSeconds();
                algorithm.finish();
                for (int j = 0; j < meansOfMeans.length; j++) {
                    meansOfMeans[j] += algorithm.getMeans()[j];
                }
                best.add(algorithm.getBest());
                worst.add(algorithm.getWorst());

            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
            System.out.println("End  : " + fileName + " :" + i);;
        }

        saveResults(fileName);
    }
    

    private void saveResults(String fileName) {
        for (int i = 0; i < meansOfMeans.length; i++) {
            meansOfMeans[i] += meansOfMeans[i] / ITERATION_PER_PARAMETERS;
        }

        Individual b = best.get(0);
        Individual w = worst.get(0);
        meansOfBests += b.getEvaluation();
        meansOfWorst += w.getEvaluation();
        for (int i = 1; i < ITERATION_PER_PARAMETERS; i++) {
            meansOfBests += best.get(i).getEvaluation();
            meansOfWorst += worst.get(i).getEvaluation();
            if (b.getEvaluation() > best.get(i).getEvaluation()) {
                b = best.get(i);
            }
            if (w.getEvaluation() < worst.get(i).getEvaluation()) {
                w = worst.get(i);
            }
        }
        meansOfBests = meansOfBests / ITERATION_PER_PARAMETERS;
        meansOfWorst = meansOfWorst / ITERATION_PER_PARAMETERS;

        try (FileWriter fw = new FileWriter(fileName + "-summary.csv")) {
            fw.append("Mean of best:" + meansOfBests + "\n");
            fw.append("Mean of worst:" + meansOfWorst + "\n");
            fw.append("Mean time of execution one:" + meanTime + "\n");
            fw.append("Best :" + b.toString() + "\n");
            fw.append("Worst:" + w.toString() + "\n");
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (FileWriter fwMeans = new FileWriter(fileName + "-summary-means.csv")) {
            fwMeans.append("MeansInIteration\n");
            for (long meansOfMean : meansOfMeans) {
                fwMeans.append(meansOfMean + "\n");
            }
            fwMeans.flush();
            fwMeans.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
