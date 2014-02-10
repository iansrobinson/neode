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

    public static Property indexableProperty( GraphDatabaseService db, String label, String name, String... indexNames )
    {
        return indexableProperty( db,label, name, new CounterBasedStringPropertyValueGenerator(), indexNames );
    }

    public static Property indexableProperty( GraphDatabaseService db, String label, String name, PropertyValueGenerator generator, String... indexNames )
    {
        return new IndexableProperty( db,label,name, generator, indexNames );
    }

    public abstract void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String label,
                                      int iteration );
}
