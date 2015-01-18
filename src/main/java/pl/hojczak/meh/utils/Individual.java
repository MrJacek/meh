/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.utils;

/**
 *
 * @author jhojczak
 */
public interface Individual extends Comparable<Individual> {

    long getEvaluation();

    void mutate();

    String toCSV();

}
