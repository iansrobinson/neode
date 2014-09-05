package org.neo4j.neode.properties;

import org.neo4j.graphdb.PropertyContainer;

class NonIndexableProperty extends Property
{
    private final String propertyName;
    private final PropertyValueGenerator generator;

    NonIndexableProperty( String propertyName, PropertyValueGenerator generator )
    {
        this.propertyName = propertyName;
        this.generator = generator;
    }

    @Override
    public void setProperty(PropertyContainer propertyContainer, String label,
                            int iteration)
    {
        propertyContainer.setProperty( propertyName,
                generator.generateValue( propertyContainer, label, iteration ) );
    }
}
