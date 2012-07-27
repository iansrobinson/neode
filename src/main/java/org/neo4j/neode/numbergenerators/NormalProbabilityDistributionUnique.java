package org.neo4j.neode.numbergenerators;

import java.util.Random;

class NormalProbabilityDistributionUnique extends BaseUniqueProbabilityDistribution
{
    protected int getNextNumber( Range minMax, Random random )
    {
        double gaussian = random.nextGaussian();
        int standardDeviation = minMax.max() / 2;
        return (int) (minMax.min() + standardDeviation + (gaussian * standardDeviation));
    }
}
