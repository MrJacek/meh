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
        p = new Problem(new double[200][200]);
    }

    @Test

    public void shouldCreateInvdividualWithGenotype() {
        Individual in = new Individual(p);
        int[] genotype = in.getGenotype();
        Assert.assertNotNull(genotype);
        Assert.assertTrue(genotype.length >= 200);
    }

    @Test
    public void shouldCreateInvdividualWithNotNullGens() {
        Individual in = new Individual(p);
        int[] genotype = in.getGenotype();
        Assert.assertNotNull(genotype);
        Assert.assertTrue(genotype.length >= 200);
        for (int i = 0; i < 200; i++) {
            Assert.assertNotNull(genotype[i]);
        }

    }
}
