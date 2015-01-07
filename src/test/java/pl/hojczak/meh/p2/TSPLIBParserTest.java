/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

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

        try {
            parser.loadGraphFromFile(TSPLIBParserTest.class.getResource("a280.xml").getFile());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(TSPLIBParserTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail(ex.getMessage());

        }
    }
}
