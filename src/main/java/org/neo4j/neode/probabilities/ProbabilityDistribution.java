/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.probabilities;

import java.util.List;
import java.util.Random;

import org.neo4j.neode.Range;

public abstract class ProbabilityDistribution
{
    public static ProbabilityDistribution flatDistribution()
    {
        return new FlatProbabilityDistributionUnique();
    }

    public static ProbabilityDistribution normalDistribution()
    {
        return new NormalProbabilityDistributionUnique();
    }

    public abstract List<Integer> generateList( Range sizeRange, Range range, Random random );

    public abstract List<Integer> generateList( int size, Range range, Random random );

    public abstract int generateSingle( Range range, Random random );
}
