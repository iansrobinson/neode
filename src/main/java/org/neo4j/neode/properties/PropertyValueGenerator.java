/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.properties;

import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.neode.Range;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public abstract class PropertyValueGenerator
{
    public static PropertyValueGenerator counterBased()
    {
        return new CounterBasedStringPropertyValueGenerator();
    }

    public static PropertyValueGenerator iterationBased()
    {
        return new IterationBasedStringPropertyValueGenerator();
    }

    public static PropertyValueGenerator idBased()
    {
        return new IdBasedStringPropertyValueGenerator();
    }

    public static PropertyValueGenerator integerRange( int min, int max,
                                                       ProbabilityDistribution probabilityDistribution )
    {
        return new RangeBasedIntegerPropertyValueGenerator( Range.minMax( min, max ), probabilityDistribution );
    }

    public static PropertyValueGenerator integerRange( int min, int max )
    {
        return integerRange( min, max, ProbabilityDistribution.flatDistribution() );
    }

    public abstract Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration );
}
