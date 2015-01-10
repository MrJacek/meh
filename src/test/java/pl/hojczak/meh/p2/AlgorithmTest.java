/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
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

    @Before
    public void initAlgorithmObject() throws FileNotFoundException {
        Properties prop = new Properties();
        p = Problem.createProblemFromFile(AlgorithmTest.class.getResource("att48.xml").getFile());
        algorithm = new Algorithm(prop, p);
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
    public void computeShouldCreateStartPopulationWithGivenSize() {
        Properties prop = new Properties();
        prop.setProperty("population.start.size", "200");
        algorithm = new Algorithm(prop, p);
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
    public void shouldCorssoverUsingPmx() {
        double[][] graph = new double[][]{{0d, 3d, 4d, 3d, 1d}, {3d, 0d, 1d, 4d, 3d}, {5d, 1d, 0d, 9d, 1d}, {1d, 2d, 3d, 0d, 2d}, {1d, 2d, 3d, 1d, 0d}};
        Problem p = new Problem(graph);
        Properties prop = new Properties();
        algorithm = new Algorithm(prop, p);
        int[] genotypeA = new int[]{0, 1, 3, 2, 4};
        int[] genotypeB = new int[]{3, 1, 2, 4, 0};
        int[] offspringGenotype = new int[]{3, 1, 2, 0, 4};
        int c = 2;
        Individual offspring = algorithm.pmx(genotypeA, genotypeB, c);
        Assert.assertArrayEquals(offspringGenotype, offspring.getGenotype());
    }

    @Test
    public void shouldCorssoverUsingPmxBiggerExample() {

        Individual parentA = new Individual(p);
        Individual parentB = new Individual(p);
        int c = (p.getSize() / 4) + Helper.getRandom().nextInt(p.getSize() / 2);
        Individual offspring = algorithm.pmx(parentA.getGenotype(), parentB.getGenotype(), c);
        System.out.println("Result:\n" + Arrays.toString(offspring.getGenotype()));
    }

    @Test
    public void shouldComputeSimpleExample() {
        Properties prop = new Properties();
        p = Problem.createProblemFromFile(AlgorithmTest.class.getResource("att48.xml").getFile());
        prop.setProperty("population.start.size", "10");
        prop.setProperty("max.iteration", "10");
        algorithm = new Algorithm(prop, p);
        algorithm.compute();
    }

}
