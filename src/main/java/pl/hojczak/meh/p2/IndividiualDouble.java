/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.util.Random;

/**
 *
 * @author jhojczak
 */
public class IndividiualDouble implements Individual {

    private IndividualImpl one;
    private IndividualImpl two;

    public IndividiualDouble() {

    }

    public IndividualImpl getOne() {
        return one;
    }

    public IndividualImpl getTwo() {
        return two;
    }

    public IndividiualDouble(IndividualImpl one, IndividualImpl two) {
        this.one = one;
        this.two = two;
        this.removeCollidations();
    }

    IndividiualDouble(Problem problem, Random random) {
        this.one = new IndividualImpl(problem, random);
        this.two = new IndividualImpl(problem, random);
        this.removeCollidations();
    }

    @Override
    public long getEvaluation() {
        return one.getEvaluation() + two.getEvaluation();
    }

    @Override
    public void mutate() {
        one.mutate();
        two.mutate();
        this.removeCollidations();
    }

    @Override
    public String toCSV() {
        return one.toCSV() + ";" + two.toCSV();
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

    public void removeCollidations(int[] one, int[] two) {
        int[] a = one;
        int[] b = two;
        b[0] = a[0];
        a[a.length - 1] = b[a.length - 1];
        for (int i = 1; i < a.length-1; i++) {
            if (a[i] == b[i]) {
                if (i >= a.length - 2) {
                    if (i % 2 == 0) {

                    } else {
                        int tmp = b[i];
                        b[i] = b[i - 1];
                        b[i - 1] = tmp;
                    }
                } else {
                    if (i % 2 == 0) {
                        int tmp = a[i];
                        a[i] = a[i + 1];
                        a[i + 1] = tmp;
                    } else {
                        int tmp = b[i];
                        b[i] = b[i + 1];
                        b[i + 1] = tmp;
                    }
                }
            }
        }
    }

    private void removeCollidations() {
        removeCollidations(one.getGenotype(), two.getGenotype());
    }

    @Override
    public String toString() {
        return "IndividiualDouble{" + "\none=" + one + "\ntwo=" + two + '}';
    }

}
