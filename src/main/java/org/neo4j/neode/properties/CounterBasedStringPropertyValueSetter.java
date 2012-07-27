package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.PropertyContainer;

class CounterBasedStringPropertyValueSetter extends PropertyValueSetter
{
    private Long counter;

    CounterBasedStringPropertyValueSetter()
    {
        counter = 1l;
    }

    @Override
    public Object setProperty( PropertyContainer propertyContainer, String propertyName, String entityName, int index, Random random )
    {
        String value = String.format( "%s-%s", entityName, counter++ );
        propertyContainer.setProperty( propertyName, value );
        return value;
    }
}
