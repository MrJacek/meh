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
public class IndividualDoubleStart extends IndividiualDouble {

    long value;

    public IndividualDoubleStart(long value) {
        super();
        this.value = value;
    }

    @Override
    public long getEvaluation() {
        return value;
    }

}
