package org.neo4j.neode.properties;

import static org.neo4j.neode.properties.CounterBasedStringPropertySetter.counterBasedPropertyValue;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

public class Property
{
    public static Property property( String name )
    {
        return new Property( name, counterBasedPropertyValue(), false );
    }

    public static Property indexableProperty( String name )
    {
        return new Property( name, counterBasedPropertyValue(), true );
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
    private Index<Node> nodeIndex;

    private Property( String propertyName, PropertySetterStrategy propertySetterStrategy, boolean isIndexable )
    {
        this.propertyName = propertyName;
        this.propertySetterStrategy = propertySetterStrategy;
        this.isIndexable = isIndexable;
    }

    public void setProperty( GraphDatabaseService db, Node node, String entityName, int index )
    {
        Object value = propertySetterStrategy.setProperty( node, propertyName, entityName, index );
        if ( isIndexable )
        {
            if ( nodeIndex == null )
            {
                nodeIndex = db.index().forNodes( entityName );
            }
            nodeIndex.add( node, propertyName, value );
        }
    }
}
