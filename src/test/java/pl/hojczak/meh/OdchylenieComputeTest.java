/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh;

import java.io.IOException;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author jhojczak
 */
public class OdchylenieComputeTest {

//    Odchylenie odchylenie = new Odchylenie();

    @Test
    public void OdchylenieComputeTest() {
        double result = StatisticHelper.odchylenieStandardowe(new double[]{2, 5, 1, 3}, 2.8d);
        result = Math.round(result * 10d) / 10d;
        System.out.println("\nResult: "+result);
        assertEquals(result, 1.5d);

    }
    @Test
    public void end2endLaunchGaussianGen() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException{
        Excecutor.main(new String[]{"/home/jhojczak/development/szkola/meh/src/main/resources/meh.properties"});
    }
}
