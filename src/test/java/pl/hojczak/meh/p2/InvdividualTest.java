/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author jhojczak
 */
public class InvdividualTest {

    Problem p;

    @Before
    public void init() {
        p = new Problem(new double[48][48]);
    }

    @Test

    public void shouldCreateInvdividualWithGenotype() {
        Individual in = new Individual(p);
        int[] genotype = in.getGenotype();
        Assert.assertNotNull(genotype);
        Assert.assertTrue(genotype.length >= 48);
    }

    @Test
    public void shouldCreateInvdividualWithNotNullGens() {
        Individual in = new Individual(p);
        int[] genotype = in.getGenotype();
        Assert.assertNotNull(genotype);
        Assert.assertTrue(genotype.length >= 48);
        for (int i = 0; i < 48; i++) {
            Assert.assertNotNull(genotype[i]);
        }

    }

    @Test
    public void shouldCreateInvdividualWithRandomGens() {
        Individual in = new Individual(p);
        int[] genotype = in.getGenotype();
        Assert.assertNotNull(genotype);
        Assert.assertTrue(genotype.length >= 48);
        for (int i = 0; i < 48; i++) {
            Assert.assertTrue("Gens should have values from vertex numbers", genotype[i] < 48);
            Assert.assertTrue("Gens should have values from vertex numbers", genotype[i] >= 0);
        }
    }

    @Test
    public void shouldCreateInvdividualWithCorrectGenotype() {
        Individual in = new Individual(p);
        int[] genotype = in.getGenotype();
        Assert.assertNotNull(genotype);
        Assert.assertTrue(genotype.length >= 48);
        checkGeanotype(genotype);
    }

    private boolean checkGeanotype(int[] genotype) {
        boolean[] tmp = new boolean[genotype.length];
        for (boolean u : tmp) {
            u = true;
        }

        for (int i = 0; i < genotype.length; i++) {
            Assert.assertTrue("Gen with value: [" + genotype[i] + "] was duplicated.", tmp[genotype[i]]);
            tmp[genotype[i]] = false;
        }
        return true;
    }
}
