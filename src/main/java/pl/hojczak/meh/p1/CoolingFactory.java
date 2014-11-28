
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.hojczak.meh.p1;

import java.util.Properties;

/**
 *
 *
 * @author jhojczak
 */
public class CoolingFactory {

    public static Cooling create(Properties prop) {
        String schemat = prop.getProperty("cooling");

        switch (schemat.toLowerCase()) {
            case "geometryczny":
                double alpha = Double.parseDouble(prop.getProperty("cooling.alpha"));
                double start = Double.parseDouble(prop.getProperty("temperature.start"));
                return new Geometryczny(start, alpha);
            case "liniowy":
                alpha = Double.parseDouble(prop.getProperty("cooling.alpha"));
                start = Double.parseDouble(prop.getProperty("temperature.start"));
                return new Liniowy(start, alpha);
            case "wykładniczy":
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            case "logarytminczny":
                start = Double.parseDouble(prop.getProperty("temperature.start"));
                return new Logarytminczny(start);
            case "other1":
                start = Double.parseDouble(prop.getProperty("temperature.start"));
                double end = Double.parseDouble(prop.getProperty("temperature.end"));
                int maxIteration = Integer.parseInt(prop.getProperty("max.iteration"));
                return new Other1(start, end, maxIteration);
            default:
                throw new IllegalArgumentException("Nie prawidłowy schemat chłodzenia");
        }
    }

    public static Cooling create(Name name,Properties prop) {
        String schemat = name.name();

        switch (schemat.toLowerCase()) {
            case "geometryczny":
                double alpha = Double.parseDouble(prop.getProperty("cooling.alpha"));
                double start = Double.parseDouble(prop.getProperty("temperature.start"));
                return new Geometryczny(start, alpha);
            case "liniowy":
                alpha = Double.parseDouble(prop.getProperty("cooling.alpha"));
                start = Double.parseDouble(prop.getProperty("temperature.start"));
                return new Liniowy(start, alpha);
            case "logarytminczny":
                start = Double.parseDouble(prop.getProperty("temperature.start"));
                return new Logarytminczny(start);
            case "other1":
                start = Double.parseDouble(prop.getProperty("temperature.start"));
                double end = Double.parseDouble(prop.getProperty("temperature.end"));
                int maxIteration = Integer.parseInt(prop.getProperty("max.iteration"));
                return new Other1(start, end, maxIteration);
            default:
                throw new IllegalArgumentException("Nie prawidłowy schemat chłodzenia");
        }
    }

    public enum Name {
        geometryczny, liniowy, logarytminczny, other1;
    }

    public interface Cooling {

        public double next();
    }

    static class Geometryczny implements Cooling {

        public Geometryczny(double start, double alpha) {
            this.alpha = alpha;
            last = start;
        }

        double alpha;
        double last;

        @Override
        public double next() {
            last = alpha * last;
            return last;
        }

    }

    static class Liniowy implements Cooling {

        double alpha;
        double last;
        int index = 0;

        public Liniowy(double start, double alpha) {
            this.alpha = alpha;
            last = start;
        }

        @Override
        public double next() {
            last = last - alpha * ++index;
            return last;
        }

    }

    static class Wykładniczy implements Cooling {

        @Override
        public double next() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    static class Logarytminczny implements Cooling {

        double last;
        double start;
        double index = 0;

        public Logarytminczny(double start) {
            this.last = start;
            this.start = start;
        }

        @Override
        public double next() {
            return start / Math.log(++index);
        }

    }

    static class Other1 implements Cooling {

        double last;
        double start;
        double end;
        double index = 0;
        int totalIteration;

        public Other1(double start, double end, int totalIteration) {
            this.last = start - 1;
            this.start = start;
            this.end = end;
            this.totalIteration = totalIteration;
        }

        @Override
        public double next() {

            last = (start - end) / (1 + Math.exp(0.3d * (++index - (totalIteration / 2)))) + end;
            return last;
        }

    }
}
