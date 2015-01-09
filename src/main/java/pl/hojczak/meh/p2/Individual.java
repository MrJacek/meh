/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author jhojczak
 */
public class Individual {

    int[] genotype;
    // arrayUsedToDetectingGenDuplicationInGenotype
    boolean[] tmp;
    double evaluation = 0;
    Problem problem;
    Helper helper = new Helper();

    public Individual(Problem problem) {
        if (problem == null) {
            throw new IllegalArgumentException("problem can't be null");
        }
        this.problem = problem;
        genotype = new int[problem.getSize()];
        createRanomGenotype();
    }

    int[] getGenotype() {
        return genotype;
    }

    double getEvaluation() {

        if (evaluation != 0) {
            return evaluation;
        }

        evaluation = 0;
        for (int i = 0; i < genotype.length; i++) {
            int next = i + 1 % genotype.length;
            evaluation += problem.getDistance(genotype[i], genotype[next]);
        }

        return evaluation;
    }

    private void createRanomGenotype() {
        for (int i = 0; i < genotype.length; i++) {
            genotype[i] = Helper.getRandom().nextInt(48);
        }
    }

    private boolean checkGeanotype(int[] genotype) {
        Arrays.fill(tmp, true);
        for (int i = 0; i < genotype.length; i++) {
            if (tmp[genotype[i]]) {
                tmp[genotype[i]] = false;
            } else {
                List<Integer> freeGens=getFreeGens(tmp);
                Helper.getRandom().nextInt(freeGens.size());
            }
        }
        return true;
    }

    private List<Integer> getFreeGens(boolean[] tmp) {
        List<Integer> result = new ArrayList<>(tmp.length / 2);
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i]) {
                result.add(i);
            }
        }
        return result;
    }
}
