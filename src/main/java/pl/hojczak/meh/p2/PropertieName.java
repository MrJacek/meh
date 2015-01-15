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
public enum PropertieName {

    OutputFile("output.file"),
    ProblemFile("problem.file"),
    MutateChance("chance.to.mutate"),
    IntercroosingChance("chance.to.intercroosing"),
    PopulationSize("population.start.size"),
    Iteration("max.iteration"),
    All("all"),
    IterationPerParam("iteration.per.param");

    public String value;

    private PropertieName(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
