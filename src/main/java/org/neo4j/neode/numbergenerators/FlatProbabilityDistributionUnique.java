package org.neo4j.neode.numbergenerators;

import java.util.Random;

class FlatProbabilityDistributionUnique extends BaseUniqueProbabilityDistribution
{
    protected int getNextNumber( Range minMax, Random random )
    {
        return minMax.min() + random.nextInt( minMax.max() + 1 );
    }
}
