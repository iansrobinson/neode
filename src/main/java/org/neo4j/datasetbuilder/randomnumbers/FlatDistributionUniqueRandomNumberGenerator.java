package org.neo4j.datasetbuilder.randomnumbers;

import java.util.Random;

public class FlatDistributionUniqueRandomNumberGenerator extends BaseUniqueRandomNumberGenerator
{
    public static RandomNumberGenerator flatDistribution(Random random)
    {
        return new FlatDistributionUniqueRandomNumberGenerator( random );
    }

    private FlatDistributionUniqueRandomNumberGenerator( Random random )
    {
        super( random );
    }

    protected int getNextNumber( int min, int upTo )
    {
        return min + random().nextInt( upTo + 1 );
    }
}
