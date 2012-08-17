package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.PropertyContainer;

class SimpleProperty extends Property
{
    private final String propertyName;
    private final PropertyValueGenerator generator;

    SimpleProperty( String propertyName, PropertyValueGenerator generator )
    {
        this.propertyName = propertyName;
        this.generator = generator;
    }

    @Override
    public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String nodeLabel,
                             int iteration, Random random )
    {
        propertyContainer.setProperty( propertyName,
                generator.generateValue( propertyContainer, nodeLabel, iteration, random ) );
    }
}
