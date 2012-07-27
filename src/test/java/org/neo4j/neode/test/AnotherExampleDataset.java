package org.neo4j.neode.test;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.DomainEntity.domainEntity;
import static org.neo4j.neode.DomainEntity.relateEntities;
import static org.neo4j.neode.commands.EntityChoices.randomChoice;
import static org.neo4j.neode.commands.NodeFinder.getOrCreate;
import static org.neo4j.neode.commands.RelationshipDescription.entities;
import static org.neo4j.neode.numbergenerators.Range.exactly;
import static org.neo4j.neode.numbergenerators.Range.minMax;
import static org.neo4j.neode.properties.Property.indexableProperty;
import static org.neo4j.neode.properties.Property.property;
import static org.neo4j.neode.properties.PropertyValueSetter.integerRange;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.commands.Dataset;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.logging.SysOutLog;

public class AnotherExampleDataset
{
    @Test
    public void buildPricingStructure()
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager datasetManager = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = datasetManager.newDataset( "Pricing tree" );

        DomainEntity root = domainEntity( "root" )
                .withProperties( indexableProperty( "name" ) )
                .build();
        DomainEntity intermediate = domainEntity( "intermediate" )
                .build();
        DomainEntity leaf = domainEntity( "leaf" )
                .withProperties( property( "price", integerRange( minMax( 5, 10 ) ) ) )
                .build();


        DomainEntityInfo roots = DomainEntity.createEntities( root )
                .quantity( 10 )
                .update( dataset );

        relateEntities( roots ).to(
                randomChoice(
                        entities( getOrCreate( intermediate, 2 ) )
                                .relationship( withName( "CONNECTED_TO" ) )
                                .relationshipConstraints( minMax( 1, 2 ) ),
                        entities( getOrCreate( leaf, 2 ) )
                                .relationship( withName( "CONNECTED_TO" ) )
                                .relationshipConstraints( exactly( 1 ) ) )
        ).update( dataset );

        dataset.end();

        db.shutdown();

    }

}
