package org.neo4j.datasetbuilder;

import static org.neo4j.datasetbuilder.IndexBasedStringPropertySetter.indexBasedPropertyValue;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class Property
{
    public static Property property( String name )
    {
        return new Property( name, indexBasedPropertyValue(), false );
    }

    public static Property indexableProperty( String name )
    {
        return new Property( name, indexBasedPropertyValue(), true );
    }

    public static Property property( String name, PropertySetterStrategy propertySetterStrategy )
    {
        return new Property( name, propertySetterStrategy, false );
    }

    public static Property indexableProperty( String name, PropertySetterStrategy propertySetterStrategy )
    {
        return new Property( name, propertySetterStrategy, true );
    }

    private final String propertyName;
    private final PropertySetterStrategy propertySetterStrategy;
    private final boolean isIndexable;

    private Property( String propertyName, PropertySetterStrategy propertySetterStrategy, boolean isIndexable )
    {
        this.propertyName = propertyName;
        this.propertySetterStrategy = propertySetterStrategy;
        this.isIndexable = isIndexable;
    }

    public void setProperty( GraphDatabaseService db, Node node, String entityName, int index )
    {
        propertySetterStrategy.setProperty( node, propertyName, entityName, index );
        if ( isIndexable )
        {
            db.index().forNodes( entityName ).add( node, propertyName, node.getProperty( propertyName ) );
        }
    }
}
