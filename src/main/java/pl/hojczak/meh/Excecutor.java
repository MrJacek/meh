/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
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
        LOG.log(Level.INFO, "Checking generator name: {0} ...", generatorName);
        Class generatorType = Class.forName(generatorName);
        if (!AbstractGenerator.class.equals(generatorType.getSuperclass())) {
            throw new IllegalArgumentException("Generator " + generatorName + " don't have correct base class.");
        }
        LOG.log(Level.INFO, "Generator {0} is correct!", generatorName);
        int invokeCount = Integer.parseInt(prop.getProperty("invoke.count"));
        int iterationPerInvoke = Integer.parseInt(prop.getProperty("generator.iteration.count"));
        double[][] result = new double[invokeCount][];
        double[] srednie = new double[invokeCount];
        double[] odchylenia = new double[invokeCount];

        LOG.info("Start use generators");

        for (int i = 0; i < invokeCount; i++) {
            LOG.log(Level.INFO, "Invoke: {0}", i);
            AbstractGenerator generator = initGenerator(generatorType, iterationPerInvoke, prop);
            result[i] = generator.generate();
            srednie[i] = StatisticHelper.wartośćOczekiwana(result[i]);
            odchylenia[i] = StatisticHelper.odchylenieStandardowe(result[i], srednie[i]);
        }
        new ResultWriter("result.csv").writeToCsv(result, srednie, odchylenia);

    }

    private static AbstractGenerator initGenerator(Class generatorType, int iterationPerInvoke, Properties prop) throws IllegalAccessException, InstantiationException {
        AbstractGenerator generator = AbstractGenerator.class.cast(generatorType.newInstance());
        generator.setIterationCount(iterationPerInvoke);
        generator.setParams(prop);
        return generator;
    }
}
