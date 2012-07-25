/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.properties;

import org.neo4j.graphdb.Node;

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

    public abstract Object setProperty( Node node, String propertyName, String entityName, int index );
}
