/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author jhojczak
 */
public class OperatorTest {

    @Test(invocationCount = 10)
    public void shouldChangeCooridnates() {
        Operator operator = new Operator(1);
        double[] param = new double[]{2d, 3d};
        double[] result = operator.mutates(param);

        System.out.println("Cor:[" + param[0] + ", " + param[1] + "]");
        System.out.println("Cor:[" + result[0] + ", " + result[1] + "]");
        Assert.assertTrue(result[0] != param[0]);
        Assert.assertTrue(result[1] != param[1]);

    }
}
