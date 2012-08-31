package org.neo4j.neode.properties;

import org.neo4j.graphdb.PropertyContainer;

class CounterBasedStringPropertyValueGenerator extends PropertyValueGenerator
{
    private Long counter;

    CounterBasedStringPropertyValueGenerator()
    {
        counter = 1l;
    }

    @Override
    public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration )
    {
        return String.format( "%s-%s", nodeLabel, counter++ );
    }
}
