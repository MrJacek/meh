/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.utils;

import java.util.Properties;

/**
 *
 * @author jhojczak
 */
public class Helper {

    public int getIntegerProp(String name, Properties prop) {
        return Integer.parseInt(prop.getProperty(name, "200"));
    }

}
