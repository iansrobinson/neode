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
        return new FlatDistributionUnique();
    }

    public static Distribution normalDistribution()
    {
        return new NormalDistributionUnique();
    }

    public abstract List<Integer> generate( Range numberOfResultsRange, Range range, Random random );

    public abstract List<Integer> generate( int numberOfResults, Range range, Random random );

    public abstract int generateSingle( Range range, Random random );
}
