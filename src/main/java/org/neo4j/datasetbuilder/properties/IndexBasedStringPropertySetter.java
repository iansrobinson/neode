package org.neo4j.datasetbuilder.properties;

import org.neo4j.graphdb.Node;

public class IndexBasedStringPropertySetter implements PropertySetterStrategy
{
    public static PropertySetterStrategy indexBasedPropertyValue()
    {
        return new IndexBasedStringPropertySetter();
    }

    private IndexBasedStringPropertySetter()
    {
    }

    @Override
    public Object setProperty( Node node, String propertyName, String entityName, int index )
    {
        String value = String.format( "%s-%s", entityName, index + 1 );
        node.setProperty( propertyName, value );
        return value;
    }
}
