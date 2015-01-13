/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author jhojczak
 */
public class Algorithm {

    private final Helper helper = new Helper();
    private List<Individual> population;
    private Properties prop;
    final private int startPopulationSize;
    private Problem problem;
    final private int maxIteration;
    final private String outputFile;
    final int chanceToIntercrossing;
    final int chanceToMutate;
    final Random random;
    FileWriter writer;
    private int[] means;

    Individual best;
    Individual worst;

    public Algorithm(Properties prop, Problem p, Random random) throws FileNotFoundException, IOException {
        if (prop == null) {
            throw new IllegalArgumentException("prop can't be null");
        }
        if (random == null) {
            throw new IllegalArgumentException("random can't be null");
        }
        if (p == null) {
            throw new IllegalArgumentException("p can't be null");
        }
        this.prop = prop;
        startPopulationSize = helper.getIntegerProp("population.start.size", prop);
        maxIteration = helper.getIntegerProp("max.iteration", prop);
        chanceToIntercrossing = helper.getIntegerProp("chance.to.mutate", prop);
        chanceToMutate = helper.getIntegerProp("chance.to.intercroosing", prop);
        outputFile = prop.getProperty("output.file", "output.csv");
        problem = p;
        writer = new FileWriter(outputFile, false);
        this.random = random;
        means = new int[maxIteration];

    }

    public Algorithm() {
        this.startPopulationSize = 0;
        this.problem = null;
        this.maxIteration = 0;
        outputFile = "output.csv";
        chanceToIntercrossing = 99;
        chanceToMutate = 10;
        random = new Random();
    }

    public void compute() throws IOException {
        createStartPopulation();
        best = new IndividualDoubleStart(Long.MIN_VALUE);
        worst = new IndividualDoubleStart(Long.MAX_VALUE);
        Collections.sort(population);
        best = population.get(0);
        worst = population.get(population.size() - 1);
        Individual localBest = best;
        Individual localWorst = worst;
        int index = 0;
        System.out.println("Iteration:");
        writer.append("index;Current population size;Reproduction population size;Childe population size;mean value;best;worst\n");
        while (index != maxIteration) {
            means[index] = calculateMean(population);
            System.out.print("\r" + index);
            List<Individual> T = reporoduction(population);
            List<Individual> O = intercroosing(T, chanceToIntercrossing);
            mutation(O, chanceToMutate);
            Collections.sort(O);
            Collections.sort(T);

            writer.append(index + ";" + population.size() + ";" + T.size() + ";" + O.size() + ";" + means[index]);

            population = succession(O, T);

            Collections.sort(population);
            localBest = population.get(0);
            localWorst = population.get(population.size() - 1);
            writer.append(";" + localBest.toCSV() + ";" + localWorst.toCSV() + "\n");
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

    public int[] getMeans() {
        return means;
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
        population = new ArrayList<>();
        for (int i = 0; i < startPopulationSize; i++) {
            IndividiualDouble in = new IndividiualDouble(problem, random);
            population.add(in);
        }
    }

    public Individual pmx(Individual parentA, Individual parentB, int c) {
        if ((parentA instanceof IndividiualDouble) && (parentB instanceof IndividiualDouble)) {
            IndividualImpl a1 = IndividiualDouble.class.cast(parentA).getOne();
            IndividualImpl a2 = IndividiualDouble.class.cast(parentA).getTwo();
            IndividualImpl b1 = IndividiualDouble.class.cast(parentB).getOne();
            IndividualImpl b2 = IndividiualDouble.class.cast(parentB).getTwo();
            IndividualImpl offspring1 = pmx(a1.getGenotype(), b1.getGenotype(), c);
            IndividualImpl offspring2 = pmx(a2.getGenotype(), b2.getGenotype(), c);
            IndividiualDouble offspring = new IndividiualDouble(offspring1, offspring2);
            return offspring;
        }
        throw new IllegalArgumentException("Simple Individual pass to pmx");
    }

    public IndividualImpl pmx(int[] parentA, int[] parentB, int c) {
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
        IndividualImpl offspring = new IndividualImpl(problem, offspringGenotype, random);
        offspring.removeDuplicationInGenotype();
        offspring.removeNotExistingConnectiong();
        return offspring;
    }

    private List<Individual> reporoduction(List<Individual> population) {
        List<Individual> result = new ArrayList<>();
        Collections.sort(population);
        for (int i = 0; i < population.size(); i++) {
            int choosen = Math.round((float) (Math.abs(random.nextGaussian()) * (double) population.size())) % population.size();
            result.add(population.get(choosen));
        }
        return result;
    }

    private List<Individual> intercroosing(List<Individual> T, int chanceToIntercrossing) {
        List<Individual> tmp = new ArrayList<>(T);
        int tmpSize = tmp.size() - 1;
        List<Individual> result = new ArrayList<>();

        while (!tmp.isEmpty()) {
            if (random.nextInt(100) < chanceToIntercrossing) {

                int a = random.nextInt(tmpSize);
                int b = random.nextInt(tmpSize);
                Individual parentA = tmp.get(a);
                Individual parentB = tmp.get(b);
                int c = Math.abs((int) Math.round(
                        (random.nextGaussian() * (problem.getSizeAsDouble() / 4))
                        + (problem.getSizeAsDouble() / 2))
                );
                result.add(pmx(parentA, parentB, c));
                c = Math.abs((int) Math.round(
                        (random.nextGaussian() * (problem.getSizeAsDouble() / 4))
                        + (problem.getSizeAsDouble() / 2))
                );
                result.add(pmx(parentB, parentA, c));

                tmp.remove(a);
                tmp.remove(b);
                tmpSize -= 2;
            }
        }
        return result;

    }

    private void mutation(List<Individual> Oi, int chanceToMutate) {
        if (random.nextInt(100) < chanceToMutate) {
            for (Individual in : Oi) {
                in.mutate();
            }
        }
    }

    private List<Individual> succession(List<Individual> newGeneration, List<Individual> oldGeneration) {
        List<Individual> result = new ArrayList<>();
        int oldSize = oldGeneration.size();
        int newSize = newGeneration.size();
        int oldSizeGroupWhichGoToNextIteration = oldSize / 4;
        int newSizeGroupWhichGoToNextIteration = (newSize / 4) * 3;
        for (int i = 0; i < oldSizeGroupWhichGoToNextIteration; i++) {
            double gaussian = random.nextGaussian();
            double tmp = Math.abs(gaussian) * (double) oldSize;
            int index = (int) Math.round(tmp);
            if (index >= oldSize) {
                index = 0;

            }
            result.add(oldGeneration.get(index));
            oldGeneration.remove(index);
            oldSize--;
        }
        for (int i = 0; i < newSizeGroupWhichGoToNextIteration; i++) {
            double gaussian = random.nextGaussian();
            double tmp = Math.abs(gaussian) * (double) newSize;
            int index = (int) Math.round(tmp);
            if (index >= newSize) {
                index = 0;
            }
            result.add(newGeneration.get(index));
            newGeneration.remove(index);
            newSize--;
        }
        return result;
    }

    void finish() throws IOException {
        writer.flush();
        writer.close();
    }

    private int calculateMean(List<Individual> population) {
        int sum = 0;
        int populationSize = 0;
        for (Individual individual : population) {
            sum += individual.getEvaluation();
            populationSize++;
        }
        return sum / populationSize;
    }
}
