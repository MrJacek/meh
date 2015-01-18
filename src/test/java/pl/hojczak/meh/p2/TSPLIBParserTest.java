/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import pl.hojczak.meh.utils.TSPLIBParser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author jhojczak
 */
public class TSPLIBParserTest {

    @Test
    public void shouldParserReadAllFile() {
        TSPLIBParser parser = new TSPLIBParser();
        long[][] graph = null;
        try {
            graph = parser.loadGraphFromFile(TSPLIBParserTest.class.getResource("att48.xml").getFile());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(TSPLIBParserTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.getMessage());
        }
        printGraph(graph);
        Assert.assertNotNull(graph);
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (i == j) {
                    Assert.assertEquals(0l, graph[i][j]);
                } else {
                    Assert.assertTrue("Distance can't be less then zero. Find between vertex [" + i + "]<->[" + j + "]", graph[i][j] > 0);
                }
            }
        }
    }

    private void printGraph(long[][] graph) {
        System.out.print("    ");
        for (int i = 0; i < graph.length; i++) {
            System.out.printf(" %06d ", i);
        }
        System.out.print("\n----");
        for (int i = 0; i < graph.length; i++) {
            System.out.print("--------");
        }
        System.out.print("\n");

        for (int i = 0; i < graph.length; i++) {
            System.out.printf("%3d|", i);
            System.out.print(" ");
            for (int j = 0; j < graph.length; j++) {

                System.out.printf("%02d  ", graph[i][j]);

            }
            System.out.print("\n");
        }
    }
}
