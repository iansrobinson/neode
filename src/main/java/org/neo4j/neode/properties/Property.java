package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.PropertyContainer;

public abstract class Property
{
    public static Property property( String name )
    {
        return new SimpleProperty( name, new CounterBasedStringPropertyValueGenerator(), false );
    }

    public static Property property( String name, PropertyValueGenerator generator )
    {
        return new SimpleProperty( name, generator, false );
    }

    public static Property indexableProperty( String name )
    {
        return new SimpleProperty( name, new CounterBasedStringPropertyValueGenerator(), true );
    }

    public static Property indexableProperty( String name, String indexName )
    {
        return new SimpleProperty( name, new CounterBasedStringPropertyValueGenerator(), true, indexName );
    }

    public static Property indexableProperty( String name, PropertyValueGenerator generator )
    {
        return new SimpleProperty( name, generator, true );
    }

    public static Property indexableProperty( String name, PropertyValueGenerator generator, String indexName )
    {
        return new SimpleProperty( name, generator, true, indexName );
    }

    public abstract void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String nodeLabel,
                                      int iteration, Random random );
}
