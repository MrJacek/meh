/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.util.Date;
import java.util.Random;

/**
 * Mutacja z rozkładem normanlnym.
 *
 * @author jhojczak
 */
public class Operator {

    private Random r = new Random(new Date().getTime());
    private final double mutationsRange;

    public Operator(double mutationRange) {
        this.mutationsRange = mutationRange;
    }

    public double[] mutates(final double[] source) {
        double[] result = source.clone();
        for (int i = 0; i < source.length; i++) {
            result[i] = source[i] + r.nextGaussian() * mutationsRange;
        }
        return result;
    }

}
