/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.util.Date;
import java.util.List;
import java.util.Random;
import pl.hojczak.meh.p1.Algorithm.SolutionsDual;

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

    public static double standardVariation(List<Solution> table, double srednia) {
        double result = 0d;
        for (Solution s : table) {
            result += Math.pow(s.getFunValue() - srednia, 2);
        }
        result = result / table.size();

        return Math.sqrt(result);
    }

    public static double standardVariationBest(List<SolutionsDual> table, double srednia) {
        double result = 0d;
        for (SolutionsDual s : table) {
            result += Math.pow(s.best.getFunValue() - srednia, 2);
        }
        result = result / table.size();

        return Math.sqrt(result);
    }

    public static double standardVariationworst(List<SolutionsDual> table, double srednia) {
        double result = 0d;
        for (SolutionsDual s : table) {
            result += Math.pow(s.worst.getFunValue() - srednia, 2);
        }
        result = result / table.size();

        return Math.sqrt(result);
    }

    public static double average(List<Solution> table) {
        double result = 0;
        for (Solution s : table) {
            result += s.getFunValue();
        }
        result /= table.size();
        return result;
    }

    public static double averageWorst(List<SolutionsDual> table) {
        double result = 0;
        for (SolutionsDual s : table) {
            result += s.worst.getFunValue();
        }
        result /= table.size();
        return result;
    }

    public static double averageBest(List<SolutionsDual> table) {
        double result = 0;
        for (SolutionsDual s : table) {
            result += s.best.getFunValue();
        }
        result /= table.size();
        return result;
    }

}
