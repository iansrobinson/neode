package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.PropertyContainer;

class IndexBasedStringPropertyValueSetter extends PropertyValueSetter
{
    @Override
    public Object setProperty( PropertyContainer propertyContainer, String propertyName, String entityName, int index, Random random )
    {
        String value = String.format( "%s-%s", entityName, index + 1 );
        propertyContainer.setProperty( propertyName, value );
        return value;
    }
}
