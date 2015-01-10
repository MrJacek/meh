/*
 * To change this license header, choose License Headers individual Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template individual the editor.
 */
package pl.hojczak.meh.p2;

import java.io.IOException;
import java.util.Arrays;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author jhojczak
 */
public class InvdividualTest {

    Problem p;
    Individual individual;

    @Before
    public void init() throws ParserConfigurationException, SAXNotSupportedException, SAXException, SAXNotRecognizedException, IOException {
        double[][] graph = new double[][]{{0d, 3d, 4d, 3d}, {3d, 0d, 0d, 4d}, {5d, 0d, 0d, 9d}, {1d, 2d, 3d, 0d}};
        p = new Problem(graph);
        individual = new Individual(p);

    }

    @Test

    public void shouldCreateInvdividualWithGenotype() {
        int[] genotype = individual.getGenotype();
        Assert.assertNotNull(genotype);
        Assert.assertTrue(genotype.length == p.getSize());
    }

    @Test
    public void shouldCreateInvdividualWithNotNullGens() {
        int[] genotype = individual.getGenotype();
        for (int i = 0; i < genotype.length; i++) {
            Assert.assertNotNull(genotype[i]);
        }

    }

    @Test
    public void shouldCreateInvdividualWithRandomGens() {
        int[] genotype = individual.getGenotype();
        for (int i = 0; i < genotype.length; i++) {
            Assert.assertTrue("Gens should have values from vertex numbers", genotype[i] < genotype.length);
            Assert.assertTrue("Gens should have values from vertex numbers", genotype[i] >= 0);
        }
    }

    @Test
    public void shouldCreateInvdividualWithGenotypeWithoutDuplicationVertex() {

        int[] genotype = new int[]{1, 1, 0, 1};

        individual.removeDuplicationInGenotype(genotype, p);
        checkGensDuplication(genotype);
        
    }

    private boolean checkGensDuplication(int[] genotype) {
        boolean[] tmp = new boolean[genotype.length];
        Arrays.fill(tmp, true);
        for (int i = 0; i < genotype.length; i++) {
            Assert.assertTrue("Gen with value: [" + genotype[i] + "] on position [" + i + "] was duplicated.", tmp[genotype[i]]);
            tmp[genotype[i]] = false;
        }
        return true;
    }

    @Test
    public void shouldCorrectGenotype() {
        individual = new Individual(p);
        int[] genotype = new int[]{0, 1, 2, 3};
        individual.removeNotExistingConnectiong(genotype,p);

        for (int i = 0; i < genotype.length; i++) {
            int from = i;
            int to = (i + 1) % genotype.length;
            double distance = p.getDistance(genotype[from], genotype[to]);
            Assert.assertTrue("Genotype have not existing conneting [" + from + "]=>[" + to + "]", 0d < distance);
        }
    }
}
