package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.neode.numbergenerators.Range;
import org.neo4j.neode.numbergenerators.Distribution;

class RangeBasedIntegerPropertyValueSetter extends PropertyValueSetter
{
    private final Range range;
    private final Distribution distribution;

    public RangeBasedIntegerPropertyValueSetter( Range range, Distribution distribution )
    {
        this.range = range;
        this.distribution = distribution;
    }

    @Override
    public Object setProperty( PropertyContainer propertyContainer, String propertyName, String entityName, int index, Random random )
    {
        int value = distribution.generateSingle( range, random );
        propertyContainer.setProperty( propertyName, value );
        return value;
    }
}
