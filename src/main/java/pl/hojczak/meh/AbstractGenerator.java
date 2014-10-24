package pl.hojczak.meh;

import java.util.Properties;

/**
 *
 * @author jhojczak
 */
public abstract class AbstractGenerator {

    protected int iterationCount;

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public void setParams(Properties prop){
//        do noting
    }

    public abstract double[] generate();

}
