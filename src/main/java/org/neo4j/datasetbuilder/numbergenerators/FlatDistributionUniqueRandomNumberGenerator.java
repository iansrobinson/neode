package org.neo4j.datasetbuilder.numbergenerators;

import java.util.Random;

public class FlatDistributionUniqueRandomNumberGenerator extends BaseUniqueNumberGenerator
{
    public static NumberGenerator flatDistribution()
    {
        return new FlatDistributionUniqueRandomNumberGenerator( );
    }

    protected int getNextNumber( int min, int upTo, Random random )
    {
        return min + random.nextInt( upTo + 1 );
    }
}
