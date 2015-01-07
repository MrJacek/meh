/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.jboss.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author jhojczak
 */
public class TSPLIBParser {

    private static final Logger LOG = Logger.getLogger(TSPLIBParser.class.getName());
    SAXParser parser;

    double[][] loadGraphFromFile(String file) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException, SAXException, IOException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        parser = parserFactory.newSAXParser();
        LOG.info("!!!Start!!!");
        SizeCounter counter = new SizeCounter();
        FileInputStream fis = new FileInputStream(file);
        parser.parse(fis, counter);
        TSPXMLHanlder readHandler = new TSPXMLHanlder(counter.getCounter());
        parser = parserFactory.newSAXParser();
        fis = new FileInputStream(file);
        parser.parse(fis, readHandler);
        return readHandler.getGraph();
    }

    public static class SizeCounter extends DefaultHandler {

        int counter = 0;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("vertex".equals(qName)) {
                counter += 1;
            }
        }

        public int getCounter() {
            return counter;
        }

    }

    public static class TSPXMLHanlder extends DefaultHandler {

        double[][] graph;
        int vertexFrom = 0;
        double currentCost = -1d;
        int vertexTo;

        public double[][] getGraph() {
            return graph;
        }

        public TSPXMLHanlder(int size) {
            graph = new double[size][size];
        }

        @Override
        public void startDocument() throws SAXException {
            LOG.info("Start dockument");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("edge".equals(qName)) {
                currentCost = Double.parseDouble(attributes.getValue("cost"));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (currentCost > 0) {
                StringBuilder b = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    b.append(ch[i + start]);
                }
                vertexTo = Integer.parseInt(b.toString());
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("vertex".equals(qName)) {
                vertexFrom++;
                vertexTo = -1;
                currentCost = -1;
            }
            if ("edge".equals(qName) && vertexTo >= 0 && currentCost > 0) {
                graph[vertexFrom][vertexTo] = currentCost;
                vertexTo = -1;
                currentCost = -1;

            }

        }
    }
}
