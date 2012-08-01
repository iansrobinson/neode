package org.neo4j.neode.test;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.NodeSpecification.nodeSpec;
import static org.neo4j.neode.NodeSpecification.relateEntities;
import static org.neo4j.neode.commands.EntityChoices.randomChoice;
import static org.neo4j.neode.commands.RelationshipDescription.getOrCreate;
import static org.neo4j.neode.numbergenerators.Range.minMax;
import static org.neo4j.neode.properties.Property.indexableProperty;
import static org.neo4j.neode.properties.Property.property;
import static org.neo4j.neode.properties.PropertyValueSetter.integerRange;

import java.util.List;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.Dataset;
import org.neo4j.neode.logging.SysOutLog;

public class AnotherExampleDataset
{
    @Test
    public void buildPricingStructure()
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager datasetManager = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = datasetManager.newDataset( "Pricing tree" );

        NodeSpecification root = nodeSpec( "root" )
                .withProperties( indexableProperty( "name" ) )
                .build();
        NodeSpecification intermediate = nodeSpec( "intermediate" )
                .build();
        NodeSpecification leaf = nodeSpec( "leaf" )
                .withProperties( property( "price", integerRange( 1, 10 ) ) )
                .build();


        NodeCollection roots = NodeSpecification.createEntities( root )
                .quantity( 10 )
                .update( dataset );

        List<NodeCollection> subnodes = relateEntities( roots ).to(
                randomChoice(
                        getOrCreate( intermediate, 20 )
                                .relationship( withName( "CONNECTED_TO" ),
                                        property( "quantity", integerRange( 1, 5 ) ) )
                                .relationshipConstraints( minMax( 1, 3 ) ),
                        getOrCreate( leaf, 100 )
                                .relationship( withName( "CONNECTED_TO" ),
                                        property( "quantity", integerRange( 1, 5 ) ) )
                                .relationshipConstraints( minMax( 1, 3 ) ) )
        ).update( dataset );

        for ( NodeCollection subnode : subnodes )
        {
            if ( subnode.entityName().equals( "intermediate" ) )
            {
                relateEntities( subnode )
                        .to( getOrCreate( leaf, 100 )
                                .relationship( withName( "CONNECTED_TO" ),
                                        property( "quantity", integerRange( 1, 5 ) ) )
                                .relationshipConstraints( minMax( 1, 3 ) ) )
                        .update( dataset );
            }
        }

        dataset.end();

        db.shutdown();

    }

}
