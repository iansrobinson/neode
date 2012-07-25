package org.neo4j.neode.numbergenerators;

import java.util.Random;

class NormalDistributionUnique extends BaseUniqueDistribution
{
    protected int getNextNumber( int min, int upTo, Random random )
    {
        double gaussian = random.nextGaussian();
        int standardDeviation = upTo / 2;
        return (int) (min + standardDeviation + (gaussian * standardDeviation));
    }
}
