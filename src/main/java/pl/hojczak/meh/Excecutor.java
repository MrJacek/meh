/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author jhojczak
 */
public class Excecutor {
    private static final Logger LOG = Logger.getLogger(Excecutor.class.getName());

    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(args[0]));

        String generatorName = prop.getProperty("generator.name");
        LOG.info("Checking generator name: " + generatorName + " ...");
        Class generatorType = Class.forName(generatorName);
        if (!AbstractGenerator.class.equals(generatorType.getSuperclass())) {
            throw new IllegalArgumentException("Generator " + generatorName + " don't have correct base class.");
        }
        LOG.info("Generator "+generatorName+" is correct!");
        int invokeCount = Integer.parseInt(prop.getProperty("invoke.count"));
        int iterationPerInvoke = Integer.parseInt(prop.getProperty("generator.iteration.count"));

        for (int i = 0; i < invokeCount; i++) {
            AbstractGenerator generator = initGenerator(generatorType, iterationPerInvoke, prop);
            double[] result = generator.generate();
            double srednia = StatisticHelper.wartośćOczekiwana(result);
            double odchylenie = StatisticHelper.odchylenieStandardowe(result, srednia);
            new ResultWriter("result_" + i + ".csv").writeToCsv(result, srednia, odchylenie);

        }
    }

    private static AbstractGenerator initGenerator(Class generatorType, int iterationPerInvoke, Properties prop) throws IllegalAccessException, InstantiationException {
        AbstractGenerator generator = AbstractGenerator.class.cast(generatorType.newInstance());
        generator.setIterationCount(iterationPerInvoke);
        generator.setParams(prop);
        return generator;
    }
}
