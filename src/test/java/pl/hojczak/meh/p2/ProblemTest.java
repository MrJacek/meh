/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.util.Properties;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author jhojczak
 */
public class ProblemTest {

    @Test
    public void problemShouldLoadStateFromProperties() {
        Problem problem = new Problem(new long[250][250]);
        Assert.assertEquals(250, problem.getSize());
    }
}
