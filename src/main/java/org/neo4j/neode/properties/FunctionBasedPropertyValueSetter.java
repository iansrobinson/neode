package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.PropertyContainer;

public class FunctionBasedPropertyValueSetter<T> extends PropertyValueSetter
{
    private final PropertyValueGenerator<T> function;

    public FunctionBasedPropertyValueSetter( PropertyValueGenerator<T> function )
    {
        this.function = function;
    }

    @Override
    public Object setProperty( PropertyContainer propertyContainer, String propertyName, String nodeLabel,
                               int iteration, Random random )
    {
        T value = function.generate( propertyContainer, propertyName, nodeLabel, iteration, random );
        propertyContainer.setProperty( propertyName, value );
        return value;
    }
}
