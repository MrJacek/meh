/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jhojczak
 */
public class Main {

    public static void main(String[] args)  {
        try {
            Properties prop=new Properties();
            prop.load(new FileInputStream(args[0]));
            Algorithm algorithm=new Algorithm(prop,Problem.createProblemFromFile(prop.getProperty("problem.file", "")));
            algorithm.compute();
            System.out.println("BEST  =  "+algorithm.getBest());
            System.out.println("WORST =  "+algorithm.getWorst());
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
