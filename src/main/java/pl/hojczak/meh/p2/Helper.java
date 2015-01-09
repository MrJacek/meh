/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author jhojczak
 */
public class Helper {

    public int getIntegerProp(String name, Properties prop) {
        return Integer.parseInt(prop.getProperty(name, "200"));
    }

    private static Random random;

    public static Random getRandom() {
        if (random == null) {
            random = new Random(new Date().getTime());
        }
        return random;

    }
}
