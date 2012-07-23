package org.neo4j.datasetbuilder.properties;

import org.neo4j.graphdb.Node;

public class NodeIdBasedStringPropertySetter implements PropertySetterStrategy
{
    public static PropertySetterStrategy nodeIdBasedPropertyValue()
    {
        return new NodeIdBasedStringPropertySetter();
    }

    private NodeIdBasedStringPropertySetter()
    {
    }

    @Override
    public Object setProperty( Node node, String propertyName, String entityName, int index )
    {
        String value = String.format( "%s-%s", entityName, node.getId() );
        node.setProperty( propertyName, value );
        return value;
    }
}
