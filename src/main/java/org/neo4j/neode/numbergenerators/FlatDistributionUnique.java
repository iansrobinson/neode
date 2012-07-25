package org.neo4j.neode.numbergenerators;

import java.util.Random;

class FlatDistributionUnique extends BaseUniqueDistribution
{
    protected int getNextNumber( int min, int upTo, Random random )
    {
        return min + random.nextInt( upTo + 1 );
    }
}
