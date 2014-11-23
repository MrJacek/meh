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

    public static Solution generate(int wymiary, double mutationsRange) {
        double[] genotyp = new double[wymiary];

        for (int i = 0; i < wymiary; i++) {
            genotyp[i] = makeCoordinate(new Random(new Date().getTime()));
        }
        return new Solution(genotyp, mutationsRange);
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

    private final Random r = new Random(new Date().getTime());
    final double[] genotyp;
    final double mutationsRange;
    Double value = null;

    public Solution(double[] genotyp, double mutationsRange) {
        this.genotyp = genotyp;
        this.mutationsRange = mutationsRange;
    }

    public int getDimension() {
        return genotyp.length;
    }

    public double getFunValue() {
        if (value != null) {
            return value;
        }

        double sum = 0;
        for (int i = 0; i < genotyp.length; i++) {
            sum += genotyp[i];
        }
        double kwadratSumy = Math.pow(sum, 2);

        double r1 = Math.pow(kwadratSumy, 0.25d);
        double r2 = Math.pow(kwadratSumy, 0.1d);
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
            result[i] = genotyp[i] + r.nextGaussian() * mutationsRange;
            if (result[i] >= 100d) {
                result[i] = 99.999999999d;
            }
            if (result[i] <= -100d) {
                result[i] = -99.999999999d;
            }
        }

        return new Solution(result, mutationsRange);
    }

    public Solution copy() {
        return new Solution(genotyp.clone(), mutationsRange);
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
