package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

public class Property
{
    public static Property property( String name )
    {
        return new Property( name, new CounterBasedStringPropertyValueSetter(), false );
    }

    public static Property indexableProperty( String name )
    {
        return new Property( name, new CounterBasedStringPropertyValueSetter(), true );
    }

    public static Property property( String name, PropertyValueSetter propertyValueSetter )
    {
        return new Property( name, propertyValueSetter, false );
    }

    public static Property indexableProperty( String name, PropertyValueSetter propertyValueSetter )
    {
        return new Property( name, propertyValueSetter, true );
    }

    private final String propertyName;
    private final PropertyValueSetter propertyValueSetter;
    private final boolean isIndexable;
    private Index<Node> nodeIndex;

    private Property( String propertyName, PropertyValueSetter propertyValueSetter, boolean isIndexable )
    {
        this.propertyName = propertyName;
        this.propertyValueSetter = propertyValueSetter;
        this.isIndexable = isIndexable;
    }

    public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String entityName,
                             int index, Random random )
    {
        Object value = propertyValueSetter.setProperty( propertyContainer, propertyName, entityName, index, random );
        if ( isIndexable && propertyContainer instanceof Node)
        {
            if ( nodeIndex == null )
            {
                nodeIndex = db.index().forNodes( entityName );
            }
            nodeIndex.add( (Node) propertyContainer, propertyName, value );
        }
    }
}
