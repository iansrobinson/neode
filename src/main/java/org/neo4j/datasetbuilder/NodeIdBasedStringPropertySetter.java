package org.neo4j.datasetbuilder;

import org.neo4j.graphdb.Node;

public class NodeIdBasedStringPropertySetter implements PropertySetterStrategy
{
    public static PropertySetterStrategy nodeIdbasedPropertyValue()
    {
        return new NodeIdBasedStringPropertySetter();
    }

    private NodeIdBasedStringPropertySetter()
    {
    }

    @Override
    public void setProperty( Node node, String propertyName, String entityName, int index )
    {
        String value = String.format( "%s-%s", entityName, node.getId() );
        node.setProperty( propertyName, value );
    }
}
