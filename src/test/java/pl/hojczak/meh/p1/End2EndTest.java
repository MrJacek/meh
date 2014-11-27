/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.io.IOException;
import java.util.Properties;
import org.testng.annotations.Test;

/**
 *
 * @author jhojczak
 */
public class End2EndTest {

    @Test
    public void shouldStart() throws IOException {
        Properties prop = new Properties();

        prop.load(End2EndTest.class.getResourceAsStream("/p1.properties"));
        Algorithm con = new Algorithm(prop);
        con.compute();
        con.saveData();

    }
}
