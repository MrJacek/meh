/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.util.Random;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author jhojczak
 */
public class OsobnikTest {

    Solution osobnik;

    @BeforeMethod
    public void init() {
        osobnik = Solution.generate(2, 1,new Random());
    }

    @Test
    public void shouldCreateOsobnik() {
        assertNotNull(osobnik);
    }

    @Test(invocationCount = 10)
    public void shouldReturnCoordinates() {
        double[] cor = osobnik.getCoordinate();
        System.out.println("cor:[" + cor[0] + "," + cor[1] + "]");
        assertEquals(cor.length, 2);
        assertTrue(cor[0] < 100);
        assertTrue(cor[0] > -100);
        assertTrue(cor[1] < 100);
        assertTrue(cor[1] < 100);
    }

    @Test
    public void shouldReturnFunValue() {

        double[] c = {0, 0};
        osobnik = new Solution(c, 1,new Random());
        double result = osobnik.getFunValue();
        assertEquals(result, 0.0);

    }

}
