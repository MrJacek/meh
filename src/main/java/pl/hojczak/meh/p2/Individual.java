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
public class Individual implements Comparable<Individual> {

    int[] genotype;
    // arrayUsedToDetectingGenDuplicationInGenotype

    double evaluation = 0;
    Problem problem;
    Helper helper = new Helper();

    public Individual() {

    }

    public Individual(Problem problem) {
        if (problem == null) {
            throw new IllegalArgumentException("problem can't be null");
        }
        this.problem = problem;
        genotype = new int[problem.getSize()];
        createRandomGenotype();
    }

    public Individual(Problem problem, int[] genotype) {
        if (problem == null) {
            throw new IllegalArgumentException("problem can't be null");
        }
        if (genotype == null) {
            throw new IllegalArgumentException("genotype can't be null");
        }
        this.problem = problem;
        this.genotype = genotype.clone();
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

    public void createRandomGenotype() {
        for (int i = 0; i < genotype.length; i++) {
            genotype[i] = Helper.getRandom().nextInt(genotype.length);
        }
        removeDuplicationInGenotype(genotype, problem);
        removeNotExistingConnectiong(genotype, problem);
    }

    public void removeDuplicationInGenotype() {
        removeDuplicationInGenotype(genotype, problem);
    }

    public void removeNotExistingConnectiong() {
        removeNotExistingConnectiong(genotype, problem);
    }

    public void mutate() {
        int c1 = Helper.getRandom().nextInt(genotype.length);
        int c2 = Helper.getRandom().nextInt(genotype.length);
        mutateInversion(genotype, c1, c2);
    }

    public void mutateInversion(int[] genotype, int c1, int c2) {
        int a = c1 < c2 ? c1 : c2;
        int b = c1 > c2 ? c1 : c2;
        while (a < b) {
            c1 = genotype[a];
            genotype[a] = genotype[b];
            genotype[b] = c1;
            a++;
            b--;
        }
    }

    public void removeDuplicationInGenotype(int[] genotype, Problem p) {
        boolean[] tmp = new boolean[genotype.length];
        Arrays.fill(tmp, true);
        for (int i = 0; i < genotype.length; i++) {
            if (tmp[genotype[i]]) {
                tmp[genotype[i]] = false;
            } else {

                List<Integer> freeGens = getFreeGens(tmp, p);
                int size = freeGens.size();
                if (size == 0) {
                    throw new IllegalArgumentException("Error during removing duplication genotype=" + Arrays.toString(genotype));
                }
                int choosenGen = Helper.getRandom().nextInt(size);
                genotype[i] = freeGens.get(choosenGen);
                if (!tmp[genotype[i]]) {
                    throw new IllegalStateException("Problem with generating free gens");
                }
                tmp[genotype[i]] = false;
            }
        }

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

    private List<Integer> getFreeGens(boolean[] tmp, Problem p) {

        List<Integer> result = new ArrayList<>(tmp.length);
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i]) {
                result.add(i);
            }
        }
        return result;
    }

    @Override
    public int compareTo(Individual o) {
        if (o == null) {
            throw new NullPointerException("o can't be null");
        }
        if (this == o) {
            return 0;
        }
        if (this.getEvaluation() > o.getEvaluation()) {
            return 1;
        } else if (this.getEvaluation() < o.getEvaluation()) {
            return -1;
        }
        return 0;

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Arrays.hashCode(this.genotype);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Individual other = (Individual) obj;
        return Arrays.equals(this.genotype, other.genotype);
    }

    @Override
    public String toString() {
        return "Individual {" + "genotype=" + Arrays.toString(genotype) + ","+String.format("evaluation=%2.2f}",evaluation);
    }
    public String toCSV(){
        return String.format("%.2f",evaluation);//+Arrays.toString(genotype).replaceAll(",", ";").replaceAll("\\[", "").replaceAll("]", "");
    }
   

}
