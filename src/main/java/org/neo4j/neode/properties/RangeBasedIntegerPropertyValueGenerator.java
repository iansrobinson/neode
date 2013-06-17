package org.neo4j.neode.properties;

import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.neode.Range;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

class RangeBasedIntegerPropertyValueGenerator extends PropertyValueGenerator
{
    private final Range range;
    private final ProbabilityDistribution probabilityDistribution;

    public RangeBasedIntegerPropertyValueGenerator( Range range, ProbabilityDistribution probabilityDistribution )
    {
        this.range = range;
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration )
    {
        return probabilityDistribution.generateSingle( range );
    }
}
