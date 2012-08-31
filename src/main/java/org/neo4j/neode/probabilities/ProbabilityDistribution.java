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
    private static final Random RANDOM_INSTANCE = new Random();

    public static ProbabilityDistribution flatDistribution()
    {
        return new FlatProbabilityDistributionUnique();
    }

    public static ProbabilityDistribution normalDistribution()
    {
        return new NormalProbabilityDistributionUnique();
    }

    private final Random random;

    protected ProbabilityDistribution()
    {
        this.random = RANDOM_INSTANCE;
    }

    public abstract List<Integer> generateList( Range sizeRange, Range range );

    public abstract List<Integer> generateList( int size, Range range );

    public abstract int generateSingle( Range range );

    public abstract String description();

    protected final Random random()
    {
        return random;
    }
}
