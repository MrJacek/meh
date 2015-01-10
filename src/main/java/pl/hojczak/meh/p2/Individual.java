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

    double evaluation = 0;
    Problem problem;
    Helper helper = new Helper();

    public Individual(Problem problem) {
        if (problem == null) {
            throw new IllegalArgumentException("problem can't be null");
        }
        this.problem = problem;
        genotype = new int[problem.getSize()];
    }

    int[] getGenotype() {
        return genotype.clone();
    }

    double getEvaluation() {

        if (evaluation != 0) {
            return evaluation;
        }

        for (int i = 0; i < genotype.length; i++) {
            int next = (i + 1) % genotype.length;
            evaluation += problem.getDistance(genotype[i], genotype[next]);
        }

        return evaluation;
    }

    public void createRanomGenotype() {
        for (int i = 0; i < genotype.length; i++) {
            genotype[i] = Helper.getRandom().nextInt(genotype.length);
        }
        removeDuplicationInGenotype(genotype, problem);
        removeNotExistingConnectiong(genotype, problem);
    }

    public void removeDuplicationInGenotype(int[] genotype, Problem p) {
        boolean[] tmp = new boolean[genotype.length];
        Arrays.fill(tmp, true);
        for (int i = 0; i < genotype.length; i++) {
            if (tmp[genotype[i]]) {
                tmp[genotype[i]] = false;
            } else {

                List<Integer> freeGens = getFreeGens(tmp, p, genotype[i - 1]);
                int choosenGen = Helper.getRandom().nextInt(freeGens.size());
                genotype[i] = freeGens.get(choosenGen);
                if (!tmp[genotype[i]]) {
                    throw new IllegalStateException("Problem with generating free gens");
                }
                tmp[genotype[i]] = false;
            }
        }

    }

    private List<Integer> getFreeGens(boolean[] tmp, Problem p, int from) {

        List<Integer> result = new ArrayList<>(tmp.length);
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i]) {
                if (problem.isConnection(from, i)) {
                    result.add(i);
                }
            }
        }
        return result;
    }

    void removeNotExistingConnectiong(int[] genotype, Problem p) {

        for (int i = 0; i < genotype.length; i++) {
            int to = (i + 1) % genotype.length;
            if (!problem.isConnection(i, to)) {
                int other;
                do {
                    other = (to + 1) % genotype.length;
                } while (!problem.isConnection(i, other)
                        && problem.isConnection(other, to));
                int tmp = genotype[to];
                genotype[to] = genotype[other];
                genotype[other] = tmp;
            }
        }
    }

}
