package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.PropertyContainer;

class IterationBasedStringPropertyValueSetter extends PropertyValueSetter
{
    @Override
    public Object setProperty( PropertyContainer propertyContainer, String propertyName, String nodeLabel,
                               int iteration, Random random )
    {
        String value = String.format( "%s-%s", nodeLabel, iteration + 1 );
        propertyContainer.setProperty( propertyName, value );
        return value;
    }
}
