package org.neo4j.neode.probabilities;

import java.util.Random;

import org.neo4j.neode.Range;

class FlatProbabilityDistributionUnique extends BaseUniqueProbabilityDistribution
{
    protected int getNextNumber( Range minMax, Random random )
    {
        return minMax.min() + random.nextInt( minMax.max() + 1 );
    }
}
