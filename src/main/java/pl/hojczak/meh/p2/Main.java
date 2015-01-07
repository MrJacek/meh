/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author jhojczak
 */
public class Main {

    public static void main(String[] argv) throws FileNotFoundException, IOException {
        Properties prop=new Properties();
        prop.load(new FileInputStream(argv[0]));
        Algorithm algorithm=new Algorithm(prop);
    }

}
