package org.neo4j.neode.probabilities;

import java.util.Random;

import org.neo4j.neode.Range;

class NormalProbabilityDistributionUnique extends BaseUniqueProbabilityDistribution
{
    protected int getNextNumber( Range minMax, Random random )
    {
        double gaussian = random.nextGaussian();
        int standardDeviation = minMax.max() / 2;
        return (int) (minMax.min() + standardDeviation + (gaussian * standardDeviation));
    }

    @Override
    public String description()
    {
        return "normal";
    }
}
