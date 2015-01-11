/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author jhojczak
 */
public class Algorithm {

    private final Helper helper = new Helper();
    private List<Individual> population;
    private Properties prop;
    final private int genotypeSize;
    final private int startPopulationSize;
    private Problem problem;
    final private int maxIteration;
    final private String outputFile;
    FileWriter writer;

    Individual best;
    Individual worst;

    public Algorithm(Properties prop, Problem p) throws FileNotFoundException, IOException {
        if (prop == null) {
            throw new IllegalArgumentException("prop can't be null");
        }
        this.prop = prop;
        startPopulationSize = helper.getIntegerProp("population.start.size", prop);
        maxIteration = helper.getIntegerProp("max.iteration", prop);
        outputFile = prop.getProperty("output.file", "output.csv");
        problem = p;
        genotypeSize = problem.getSize();
        writer = new FileWriter(outputFile, false);

    }

    public Algorithm() {
        this.genotypeSize = 0;
        this.startPopulationSize = 0;
        this.problem = null;
        this.maxIteration = 0;
        outputFile = "output.csv";
    }

    public void compute() throws IOException {
        createStartPopulation();
        best = new IndividualStart(Double.MIN_VALUE);
        worst = new IndividualStart(Double.MAX_VALUE);
        Collections.sort(population);
        best = population.get(0);
        worst = population.get(population.size() - 1);
        Individual localBest = best;
        Individual localWorst = worst;
        int index = 0;
        System.out.println("Iteration:");
        while (index != maxIteration) {
            System.out.print("\r"+index);
            List<Individual> T = reporoduction(population);
            List<Individual> O = intercroosing(T);
            mutation(O);
            Collections.sort(O);
            Collections.sort(T);
            writer.append(population.size()+";" + T.size() + ";" + O.size());
            population = succession(O, T);
            Collections.sort(population);
            localBest = population.get(0);
            localWorst = population.get(population.size() - 1);
            writer.append(";"+localBest.toCSV()+";"+localWorst.toCSV()+"\n");
            if (localBest.getEvaluation() < best.getEvaluation()) {
                best = localBest;
            }
            if (localWorst.getEvaluation() > worst.getEvaluation()) {
                worst = localWorst;
            }
            
            
            index++;
        }
        System.out.println("\nFinish!");
    }

    public Individual getBest() {
        return best;
    }

    public Individual getWorst() {
        return worst;
    }

    public List<Individual> getCurrentPopulation() {
        return population;
    }

    public void createStartPopulation() {
        population = new LinkedList<>();
        for (int i = 0; i < startPopulationSize; i++) {
            Individual in = new Individual(problem);
            population.add(in);
        }
    }

    public Individual pmx(int[] parentA, int[] parentB, int c) {
        int[] offspringGenotype = parentA.clone();
        if (c < 0) {
            c = 0;
        }
        if (c >= offspringGenotype.length) {
            c = offspringGenotype.length / 2;
        }
        for (int i = 0; i <= c; i++) {
            for (int j = 0; j < offspringGenotype.length; j++) {
                if (parentB[i] == offspringGenotype[j]) {
                    offspringGenotype[j] = offspringGenotype[i];
                    offspringGenotype[i] = parentB[i];
                    break;
                }
            }
        }
        Individual offspring = new Individual(problem, offspringGenotype);
        offspring.removeDuplicationInGenotype();
        offspring.removeNotExistingConnectiong();
        return offspring;
    }

    private List<Individual> reporoduction(List<Individual> population) {
        List<Individual> result = new LinkedList<>();
        Collections.sort(population);
        for (int i = 0; i < population.size(); i++) {
            int choosen = Math.round((float) (Math.abs(Helper.getRandom().nextGaussian()) * (double) population.size())) % population.size();
            result.add(population.get(choosen));
        }
        return result;
    }

    private List<Individual> intercroosing(List<Individual> T) {
        List<Individual> tmp = new LinkedList<>(T);
        int tmpSize = tmp.size() - 1;
        List<Individual> result = new LinkedList<>();

        while (!tmp.isEmpty()) {
            int a = Helper.getRandom().nextInt(tmpSize);
            int b = Helper.getRandom().nextInt(tmpSize);
            Individual parentA = tmp.get(a);
            Individual parentB = tmp.get(b);
            int c = Math.abs((int) Math.round(
                    (Helper.getRandom().nextGaussian() * (problem.getSizeAsDouble() / 4))
                    + (problem.getSizeAsDouble() / 2))
            );
            result.add(pmx(parentA.getGenotype(), parentB.getGenotype(), c));
            c = Math.abs((int) Math.round(
                    (Helper.getRandom().nextGaussian() * (problem.getSizeAsDouble() / 4))
                    + (problem.getSizeAsDouble() / 2))
            );
            result.add(pmx(parentB.getGenotype(), parentA.getGenotype(), c));

            tmp.remove(a);
            tmp.remove(b);
            tmpSize -= 2;
        }
        return result;

    }

    private void mutation(List<Individual> Oi) {
        for (Individual in : Oi) {
            in.mutate();
            in.removeNotExistingConnectiong();
        }
    }

    private List<Individual> succession(List<Individual> newGeneration, List<Individual> oldGeneration) {
        List<Individual> result = new LinkedList<>();
        int oldSize = oldGeneration.size();
        int newSize = newGeneration.size();
        int oldSizeGroupWhichGoToNextIteration = oldSize / 4;
        int newSizeGroupWhichGoToNextIteration = (newSize / 4) * 3;
        for (int i = 0; i < oldSizeGroupWhichGoToNextIteration; i++) {
            double gaussian = Helper.getRandom().nextGaussian();
            double tmp = Math.abs(gaussian) * (double) oldSize;
            int index = (int) Math.round(tmp);
            if (index >= oldSize) {
                index = 0;

            }
            result.add(oldGeneration.get(index));
        }
        for (int i = 0; i < newSizeGroupWhichGoToNextIteration; i++) {
            double gaussian = Helper.getRandom().nextGaussian();
            double tmp = Math.abs(gaussian) * (double) newSize;
            int index = (int) Math.round(tmp);
            if (index >= newSize) {
                index = 0;
            }
            result.add(newGeneration.get(index));
        }
        return result;
    }

    void finish() throws IOException {
      writer.flush();
      writer.close();
    }

}
