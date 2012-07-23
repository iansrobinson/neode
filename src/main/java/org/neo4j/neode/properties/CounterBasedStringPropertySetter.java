package org.neo4j.neode.properties;

import org.neo4j.graphdb.Node;

public class CounterBasedStringPropertySetter implements PropertySetterStrategy
{
    public static PropertySetterStrategy counterBasedPropertyValue()
    {
        return new CounterBasedStringPropertySetter();
    }

    private Long counter;

    private CounterBasedStringPropertySetter()
    {
        counter = 1l;
    }

    @Override
    public Object setProperty( Node node, String propertyName, String entityName, int index )
    {
        String value = String.format( "%s-%s", entityName, counter++ );
        node.setProperty( propertyName, value );
        return value;
    }
}
