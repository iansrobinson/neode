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

    public static Property indexableProperty( GraphDatabaseService db, String label, String name, String... labelNames )
    {
        return indexableProperty( db,label, name, new CounterBasedStringPropertyValueGenerator(), labelNames );
    }

    public static Property indexableProperty( GraphDatabaseService db, String label, String name, PropertyValueGenerator generator, String... labelNames )
    {
        return new IndexableProperty( db,label,name, generator, labelNames );
    }

    public abstract void setProperty(PropertyContainer propertyContainer, String label,
                                     int iteration);
}
