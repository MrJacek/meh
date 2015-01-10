/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author jhojczak
 */
public class Algorithm {

    private final Helper helper = new Helper();
    private List<Individual> population;
    private Properties prop;
    final private int genotypeSize;
    final private int startPopulationSize;
    final private Problem problem;

    public Algorithm(Properties prop) throws FileNotFoundException {
        if (prop == null) {
            throw new IllegalArgumentException("prop can't be null");
        }
        this.prop = prop;
        startPopulationSize = helper.getIntegerProp("population.start.size", prop);
        problem = Problem.createProblemFromFile(prop.getProperty("problem.graph.file", ""));
        genotypeSize = problem.getSize();

    }

    public void compute() {
        createStartPopulation();

    }

    public List<Individual> getCurrentPopulation() {
        return population;
    }

    public void createStartPopulation() {
        population = new LinkedList<>();
        for (int i = 0; i < startPopulationSize; i++) {
            Individual in = new Individual(problem);
            in.createRanomGenotype();
            population.add(in);
        }

    }
}
