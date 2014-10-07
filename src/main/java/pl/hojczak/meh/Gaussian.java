/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author jhojczak
 */
public class Gaussian extends AbstractGenerator {

    Random r = new Random(new Date().getTime());
    @Override
    public double[] generate() {
        double[] result = new double[iterationCount];
        for (int i = 0; i < iterationCount; i++) {
            result[i] = r.nextGaussian();
        }
        return result;
    }
}
