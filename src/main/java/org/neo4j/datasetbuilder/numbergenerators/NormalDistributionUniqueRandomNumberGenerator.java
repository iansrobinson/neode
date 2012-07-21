package org.neo4j.datasetbuilder.numbergenerators;

import java.util.Random;

public class NormalDistributionUniqueRandomNumberGenerator extends BaseUniqueNumberGenerator
{
    public static NumberGenerator normalDistribution()
    {
        return new NormalDistributionUniqueRandomNumberGenerator( );
    }

    protected int getNextNumber( int min, int upTo, Random random )
    {
        double gaussian = random.nextGaussian();
        int standardDeviation = upTo / 2;
        return (int) (min + standardDeviation + (gaussian * standardDeviation));
    }
}
