package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.PropertyContainer;

public abstract class Property
{
    public static Property property( String name )
    {
        return new SimpleProperty( name, new CounterBasedStringPropertyValueSetter(), false );
    }

    public static Property indexableProperty( String name )
    {
        return new SimpleProperty( name, new CounterBasedStringPropertyValueSetter(), true );
    }

    public static Property property( String name, PropertyValueSetter propertyValueSetter )
    {
        return new SimpleProperty( name, propertyValueSetter, false );
    }

    public static Property indexableProperty( String name, PropertyValueSetter propertyValueSetter )
    {
        return new SimpleProperty( name, propertyValueSetter, true );
    }

    public abstract void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String nodeLabel,
                             int iteration, Random random );
}
