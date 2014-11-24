/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author jhojczak
 */
public class Solution {

    public static Solution generate(int wymiary, double mutationsRange, Random generator) {
        double[] genotyp = new double[wymiary];

        for (int i = 0; i < wymiary; i++) {
            genotyp[i] = makeCoordinate(new Random(new Date().getTime()));
        }
        return new Solution(genotyp, mutationsRange, generator);
    }

    public static double makeCoordinate(Random random) {
        double tmp = (random.nextGaussian() * 100);
        if (tmp >= 100d) {
            tmp = 99.999999999d;
        }
        if (tmp <= -100d) {
            tmp = -99.999999999d;
        }
        return tmp;
    }

    private final Random generator;
    final double[] genotyp;
    final double mutationsRange;
    Double value = null;

    public Solution(double[] genotyp, double mutationsRange, Random generator) {
        this.genotyp = genotyp;
        this.mutationsRange = mutationsRange;
        this.generator = generator;
    }

    public int getDimension() {
        return genotyp.length;
    }

    public double getFunValue() {
        if (value != null) {
            return value;
        }

        double sumaKwadratów = 0;
        for (int i = 0; i < genotyp.length; i++) {
            sumaKwadratów += Math.pow(genotyp[i], 2);
        }

        double r1 = Math.pow(sumaKwadratów, 0.25d);
        double r2 = Math.pow(sumaKwadratów, 0.1d);
        r2 = Math.pow(Math.sin(50 * r2), 2) + 1;
        value = r1 * r2;
        return value;
    }

    public double[] getCoordinate() {
        return genotyp.clone();
    }

    public Solution mutates() {
        double[] result = genotyp.clone();
        for (int i = 0; i < genotyp.length; i++) {
            result[i] = genotyp[i] + generator.nextGaussian() * mutationsRange;
            if (result[i] >= 100d) {
                result[i] = 99.999999999d;
            }
            if (result[i] <= -100d) {
                result[i] = -99.999999999d;
            }
        }

        return new Solution(result, mutationsRange, new Random(generator.nextLong()));
    }

    public Solution copy() {
        return new Solution(genotyp.clone(), mutationsRange, new Random(generator.nextLong()));
    }

    @Override
    public String toString() {
        StringBuilder resutl = new StringBuilder();
        for (double d : genotyp) {
            resutl.append(String.format("%.3f;", d));
        }
        resutl.append(String.format("%.3f;", value));
        return resutl.toString();

    }

}
