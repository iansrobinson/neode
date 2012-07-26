package org.neo4j.neode.numbergenerators;

import java.util.Random;

class FlatDistributionUnique extends BaseUniqueDistribution
{
    protected int getNextNumber( Range minMax, Random random )
    {
        return minMax.min() + random.nextInt( minMax.max() + 1 );
    }
}
