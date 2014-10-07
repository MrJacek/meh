/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh;

import java.util.Properties;

/**
 *
 * @author jhojczak
 */
public abstract class AbstractGenerator {

    protected int iterationCount;

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public void setParams(Properties prop){
//        do noting
    }

    public abstract double[] generate();

}
