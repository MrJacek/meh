/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jhojczak
 */
public class AlgorithmTest {

    Algorithm algorithm;
    Problem p;
    Random random = new Random();

    @Before
    public void initAlgorithmObject() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        p = Problem.createProblemFromFile(AlgorithmTest.class.getResource("att48.xml").getFile());
        algorithm = new Algorithm(prop, p, random, false);
    }

    @Test
    public void computeShouldCreateStartPopulation() {
        algorithm.createStartPopulation();
        List<Individual> population = algorithm.getCurrentPopulation();
        Assert.assertNotNull(population);
        Assert.assertTrue(population.size() > 0);
        Assert.assertTrue(population.size() >= 2);
    }

    @Test
    public void computeShouldCreateStartPopulationWithGivenSize() throws IOException {
        Properties prop = new Properties();
        prop.setProperty("population.start.size", "200");
        algorithm = new Algorithm(prop, p, random, false);
        algorithm.createStartPopulation();
        List<Individual> population = algorithm.getCurrentPopulation();
        Assert.assertNotNull(population);
        Assert.assertEquals(200, population.size());
    }

    @Test
    public void computeShouldEvaluateStartPopulation() {
        algorithm.createStartPopulation();
        List<Individual> population = algorithm.getCurrentPopulation();
        Assert.assertNotNull(population);
        for (Individual indiv : population) {
            double result = indiv.getEvaluation();
            Assert.assertNotNull(result);
            Assert.assertNotEquals(0, result);
        }
    }

    @Test
    public void shouldCorssoverUsingPmx() throws IOException {
        long[][] graph = new long[][]{{0l, 3l, 4l, 3l, 1l}, {3l, 0l, 1l, 4l, 3l}, {5l, 1l, 0l, 9l, 1l}, {1l, 2l, 3l, 0l, 2l}, {1l, 2l, 3l, 1l, 0l}};
        Problem p = new Problem(graph);
        Properties prop = new Properties();
        algorithm = new Algorithm(prop, p, random,false);
        int[] genotypeA = new int[]{0, 1, 3, 2, 4};
        int[] genotypeB = new int[]{3, 1, 2, 4, 0};
        int[] offspringGenotype = new int[]{3, 1, 2, 0, 4};
        int c = 2;
        IndividualImpl offspring = algorithm.pmx(genotypeA, genotypeB, c);
        Assert.assertArrayEquals(offspringGenotype, offspring.getGenotype());
    }

    @Test
    public void shouldCorssoverUsingPmxBiggerExample() {

        IndividualImpl parentA = new IndividualImpl(p, random);
        IndividualImpl parentB = new IndividualImpl(p, random);
        int c = (p.getSize() / 4) + random.nextInt(p.getSize() / 2);
        IndividualImpl offspring = algorithm.pmx(parentA.getGenotype(), parentB.getGenotype(), c);
        System.out.println("Result:\n" + Arrays.toString(offspring.getGenotype()));
    }

    @Test
    public void shouldComputeSimpleExample() throws IOException {
        Properties prop = new Properties();
        p = Problem.createProblemFromFile(AlgorithmTest.class.getResource("att48.xml").getFile());
        prop.setProperty("population.start.size", "10");
        prop.setProperty("max.iteration", "10");
        algorithm = new Algorithm(prop, p, random,false);
        algorithm.compute();
    }

}
