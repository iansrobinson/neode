package org.neo4j.datasetbuilder.randomnumbers;

import java.util.Random;

public class NormalDistributionUniqueRandomNumberGenerator extends BaseUniqueRandomNumberGenerator
{
    public static RandomNumberGenerator normalDistribution( Random random )
    {
        return new NormalDistributionUniqueRandomNumberGenerator( random );
    }

    private NormalDistributionUniqueRandomNumberGenerator( Random random )
    {
        super( random );
    }

    protected int getNextNumber( int min, int upTo )
    {
        double gaussian = random().nextGaussian();
        int standardDeviation = upTo / 2;
        return (int) (min + standardDeviation + (gaussian * standardDeviation));
    }
}
