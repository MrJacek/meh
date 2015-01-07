/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

/**
 *
 * @author jhojczak
 */
public class Individual {

    int[] genotype;
    Problem problem;

    public Individual(Problem problem) {
        if (problem == null) {
            throw new IllegalArgumentException("problem can't be null");
        }
        this.problem = problem;
        genotype = new int[problem.getSize()];
    }

    int[] getGenotype() {
        return genotype;
    }

    int getEvaluation() {
        int result = 0;
        for (int i = 0; i < genotype.length; i++) {
            int next = i + 1 % genotype.length;
            result = problem.getDistance(genotype[i], genotype[next]);
        }
        return result;
    }

}
