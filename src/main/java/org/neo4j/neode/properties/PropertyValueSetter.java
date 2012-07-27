/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;
import org.neo4j.neode.numbergenerators.Range;

public abstract class PropertyValueSetter
{
    public static PropertyValueSetter counterBased()
    {
        return new CounterBasedStringPropertyValueSetter();
    }

    public static PropertyValueSetter indexBased()
    {
        return new IndexBasedStringPropertyValueSetter();
    }

    public static PropertyValueSetter nodeIdBased()
    {
        return new IdBasedStringPropertyValueSetter();
    }

    public static PropertyValueSetter integerRange( int min, int max, ProbabilityDistribution probabilityDistribution )
    {
        return new RangeBasedIntegerPropertyValueSetter( Range.minMax( min, max ), probabilityDistribution );
    }

    public static PropertyValueSetter integerRange( int min, int max )
    {
        return new RangeBasedIntegerPropertyValueSetter( Range.minMax( min, max ), ProbabilityDistribution.flatDistribution() );
    }

    public abstract Object setProperty( PropertyContainer propertyContainer, String propertyName, String entityName,
                                        int index, Random random );
}
