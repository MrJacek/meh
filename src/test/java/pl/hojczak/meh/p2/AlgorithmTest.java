/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileNotFoundException;
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

    @Before
    public void initAlgorithmObject() throws FileNotFoundException {
        Properties prop = new Properties();
        prop.setProperty("problem.graph.file", AlgorithmTest.class.getResource("att48.xml").getFile());
        algorithm = new Algorithm(prop);
    }

    @Test
    public void shouldCouldInvokeComputeOnAlgorithm() {
        algorithm.compute();
    }

    @Test
    public void computeShouldCreateStartPopulation() {
        algorithm.compute();
        List<Individual> population = algorithm.getCurrentPopulation();
        Assert.assertNotNull(population);
        Assert.assertTrue(population.size() > 0);
        Assert.assertTrue(population.size() >= 2);
    }

    @Test
    public void computeShouldCreateStartPopulationWithGivenSize() {
        algorithm.compute();
        List<Individual> population = algorithm.getCurrentPopulation();
        Assert.assertNotNull(population);
        Assert.assertEquals(200, population.size());
    }

    @Test
    public void computeShouldEvaluateStartPopulation() {
        algorithm.compute();
        List<Individual> population = algorithm.getCurrentPopulation();
        Assert.assertNotNull(population);
        for (Individual indiv : population) {
            int result = indiv.getEvaluation();
            Assert.assertNotNull(result);
            Assert.assertNotEquals(0, result);
        }

    }

}
