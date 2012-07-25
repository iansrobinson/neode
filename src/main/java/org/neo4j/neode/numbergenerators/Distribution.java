/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.numbergenerators;

import java.util.List;
import java.util.Random;

public abstract class Distribution
{
    public static Distribution flatDistribution()
    {
        return new FlatDistributionUnique( );
    }

    public static Distribution normalDistribution()
    {
        return new NormalDistributionUnique( );
    }

    public abstract List<Integer> generate( int minNumberOfResults, int maxNumberOfResults,
                                            int min, int max, Random random );
    public abstract List<Integer> generate( int numberOfResults, int min, int max, Random random );
    public abstract int generateSingle( int min, int max, Random random );
}
