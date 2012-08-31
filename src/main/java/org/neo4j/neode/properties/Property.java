package org.neo4j.neode.properties;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.PropertyContainer;

public abstract class Property
{
    public static Property property( String name )
    {
        return new NonIndexableProperty( name, new CounterBasedStringPropertyValueGenerator() );
    }

    public static Property property( String name, PropertyValueGenerator generator )
    {
        return new NonIndexableProperty( name, generator );
    }

    public static Property indexableProperty( String name )
    {
        return new IndexableProperty( name, new CounterBasedStringPropertyValueGenerator() );
    }

    public static Property indexableProperty( String name, String indexName )
    {
        return new IndexableProperty( name, new CounterBasedStringPropertyValueGenerator(), indexName );
    }

    public static Property indexableProperty( String name, PropertyValueGenerator generator )
    {
        return new IndexableProperty( name, generator );
    }

    public static Property indexableProperty( String name, PropertyValueGenerator generator, String indexName )
    {
        return new IndexableProperty( name, generator, indexName );
    }

    public abstract void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String label,
                                      int iteration );
}
