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
public class StatisticHelper {

    private final Random random = new Random(new Date().getTime());

    public double makeCoordinate() {
        double tmp = (random.nextGaussian() * 100);
        if (tmp >= 100d) {
            tmp = 99.999999999d;
        }
        if (tmp <= -100d) {
            tmp = -99.999999999d;
        }
        return tmp;
    }

    public static double odchylenieStandardowe(double[] table, double srednia) {
        double result = 0d;
        for (double index : table) {
            result += Math.pow(index - srednia, 2);
        }
        result = result / table.length;

        return Math.sqrt(result);
    }

    public static double wartośćOczekiwana(double[] table) {
        double result = 0;
        for (double i : table) {
            result += i;
        }
        result /= table.length;
        return result;
    }

}
