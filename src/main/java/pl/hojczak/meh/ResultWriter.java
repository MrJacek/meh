/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author jhojczak
 */
public class ResultWriter {

    String resultFile;

    ResultWriter(String string) {
        resultFile = string;
    }

    void writeToCsv(double[] result) throws IOException {
        try (FileWriter writer = new FileWriter(resultFile)) {
            for (double d : result) {
                writer.append(String.format("%.2f\n", d));
            }
            writer.flush();
        }
    }

    void writeToCsv(double[][] result, double[] srednia, double[] odchylenie) throws IOException {
        try (FileWriter writer = new FileWriter(resultFile)) {

            for (int i = 0; i < srednia.length; i++) {
                writer.append(String.format("%.2f;", srednia[i]));
            }
            writer.append("\n");
            for (int i = 0; i < odchylenie.length; i++) {
                writer.append(String.format("%.2f;", odchylenie[i]));
            }
            writer.append("\n");

            for (int indexInProbe = 0; indexInProbe < result[0].length; ++indexInProbe) {
                for (int i = 0; i < result.length; i++) {
                    writer.append(String.format("%.2f;", result[i][indexInProbe]));
                }
                writer.append("\n");
            }
            writer.flush();
        }
    }
}
