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
public class ResultWriter  {

    String resultFile;
    ResultWriter(String string) {
        resultFile=string;
    }

    void writeToCsv(double[] result) throws IOException {
        try (FileWriter writer = new FileWriter(resultFile)) {
            for(double d : result){
                writer.append(String.format("%.2f\n", d));
            }
            writer.flush();
        }
    }
    void writeToCsv(double[] result,double srednia,double odchylenie) throws IOException {
        try (FileWriter writer = new FileWriter(resultFile)) {
            for(double d : result){
                writer.append(String.format("%.2f\n", d));
            }
            writer.append("\"=====Srednia======\"\n");
            writer.append(String.format("%.2f\n", srednia));
            writer.append("\"=====Odchylenie======\"\n");
            writer.append(String.format("%.2f\n", odchylenie));
            writer.flush();
        }
    }
}
