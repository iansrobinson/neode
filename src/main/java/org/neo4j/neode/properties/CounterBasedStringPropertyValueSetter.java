package org.neo4j.neode.properties;

import org.neo4j.graphdb.Node;

class CounterBasedStringPropertyValueSetter extends PropertyValueSetter
{
    private Long counter;

    CounterBasedStringPropertyValueSetter()
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
