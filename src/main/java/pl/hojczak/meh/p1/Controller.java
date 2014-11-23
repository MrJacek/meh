/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import pl.hojczak.meh.p1.CoolingFactory.Cooling;

/**
 *
 * @author jhojczak
 */
public class Controller {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(args[0]));
        Controller cont = new Controller(prop);
        cont.saveData();

    }
    Random r = new Random(new Date().getTime());
    int max = 10;
    double k;
    double T;
    Cooling cooling;
    List<Solution> bestResults = new ArrayList<>();
    List<Double> temps = new ArrayList<>();

    int dimensions;
    double mutationsRange;
    String resultFile;

    public Controller(Properties prop) {
        max = Integer.parseInt(prop.getProperty("max.iteration"));
        cooling = CoolingFactory.create(prop);
        dimensions = Integer.parseInt(prop.getProperty("dimensions"));
        mutationsRange = Double.parseDouble(prop.getProperty("mutations.range"));
        resultFile = prop.getProperty("result.file");
        T = Double.parseDouble(prop.getProperty("cooling.start"));
        k = Double.parseDouble(prop.getProperty("k"));
    }

    public void saveData() throws IOException {
        try (FileWriter writer = new FileWriter(resultFile)) {
            writer.append("id;");
            for (int i = 0; i < dimensions; i++) {
                writer.append("x" + (i+1) + ";");
            }
            writer.append("value;");
            writer.append("temp\n");
            for (int i = 0; i < bestResults.size(); i++) {
                
                writer.append(i + ";" + bestResults.get(i).toString() +String.format("%.3f", temps.get(i)) + "\n");
            }
            writer.flush();
        }

    }

    public void compute() {

        Solution xStar = Solution.generate(dimensions, mutationsRange);
        Solution x = xStar.copy();
        int globalIndex = 100;
        do {
            int index = 0;
            do {
                Solution xPrime = x.mutates();
                if (xPrime.getFunValue() < x.getFunValue()) { //minimalizacja, przy maksymalizacji odwrotnie
                    x = xPrime.copy();  //jeśli nowe rozwiązanie jest lepsze, to jest zawsze akceptowane
                    xStar = x.copy();
                } else {
                    double y = r.nextDouble();
                    if (y < (Math.exp(-1d * (xPrime.getFunValue() - x.getFunValue()) / (k * T)))) {
                        x = xPrime.copy();
                        if (x.getFunValue() < xStar.getFunValue()) {
                            xStar = x.copy();
                        }
                    }
                }
                index = index + 1;
            } while (index < max); //MAX – liczba powtórzeń przy stałej temperaturze
            bestResults.add(xStar);
            T = cooling.next();
            temps.add(T);
            globalIndex--;
        } while (globalIndex > 0);
    }

}
