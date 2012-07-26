/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.numbergenerators.Range;
import org.neo4j.neode.numbergenerators.Distribution;

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
        return new NodeIdBasedStringPropertyValueSetter();
    }

    public static PropertyValueSetter integerRangeBased( Range range, Distribution distribution )
    {
        return new RangeBasedIntegerPropertyValueSetter( range, distribution );
    }

    public static PropertyValueSetter integerRange( Range range )
    {
        return new RangeBasedIntegerPropertyValueSetter( range, Distribution.flatDistribution() );
    }

    public abstract Object setProperty( Node node, String propertyName, String entityName, int index, Random random );
}
