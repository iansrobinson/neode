package org.neo4j.neode.properties;

import org.neo4j.graphdb.PropertyContainer;

class IterationBasedStringPropertyValueGenerator extends PropertyValueGenerator
{
    @Override
    public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration )
    {
        return String.format( "%s-%s", nodeLabel, iteration + 1 );
    }
}
