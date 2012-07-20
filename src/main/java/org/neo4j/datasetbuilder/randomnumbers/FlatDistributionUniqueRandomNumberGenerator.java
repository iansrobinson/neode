package org.neo4j.datasetbuilder.randomnumbers;

import java.util.Random;

public class FlatDistributionUniqueRandomNumberGenerator extends BaseUniqueRandomNumberGenerator
{
    public static RandomNumberGenerator flatDistribution()
    {
        return new FlatDistributionUniqueRandomNumberGenerator( );
    }

    protected int getNextNumber( int min, int upTo, Random random )
    {
        return min + random.nextInt( upTo + 1 );
    }
}
