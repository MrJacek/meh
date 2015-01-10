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
public class IndividualStart extends Individual {

    public IndividualStart(final double evaluation) {
        super();
        this.evaluation = evaluation;
    }

    @Override
    double getEvaluation() {
        return evaluation;
    }

}
