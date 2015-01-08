/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 *
 * @author jhojczak
 */
public class Problem {

    private final Helper helper = new Helper();
    private double[][] graph;

    public Problem(double[][] graph) {
        if (graph == null) {
            throw new IllegalArgumentException("graph can't be null");
        }
        this.graph = graph;
    }

    public int getSize() {
        return graph.length;
    }

    public int getDistance(int genotype, int genotype0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadProblemFromProperties(Properties prop) {
        prop.getProperty("problem.graph.file", "");
    }

    public static Problem createProblemFromFile(String file) {
        TSPLIBParser parser = new TSPLIBParser();
        try {
            double[][] graph = parser.loadGraphFromFile(file);
            return new Problem(graph);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
