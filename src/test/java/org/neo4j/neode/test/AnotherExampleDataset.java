package org.neo4j.neode.test;

import static org.neo4j.neode.DomainEntity.domainEntity;
import static org.neo4j.neode.numbergenerators.Range.minMax;
import static org.neo4j.neode.properties.Property.indexableProperty;
import static org.neo4j.neode.properties.PropertyValueSetter.integerRange;

import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.properties.Property;

public class AnotherExampleDataset
{
    public void buildPricingStructure()
    {
        DomainEntity root = domainEntity( "root" )
                .withProperties( indexableProperty( "name" ) )
                .build();
        DomainEntity intermediate = domainEntity( "intermediate" )
                .build();
        DomainEntity leaf = domainEntity( "leaf" )
                .withProperties( Property.property( "price", integerRange( minMax( 5, 10 ) ) ) )
                .build();

    }

}
