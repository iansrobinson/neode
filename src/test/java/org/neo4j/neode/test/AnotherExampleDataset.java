package org.neo4j.neode.test;

import static org.neo4j.neode.NodeSpecification.nodeSpec;
import static org.neo4j.neode.NodeSpecification.relateNodes;
import static org.neo4j.neode.commands.NodeChoices.randomChoice;
import static org.neo4j.neode.commands.RelationshipSpecification.getOrCreate;
import static org.neo4j.neode.numbergenerators.Range.minMax;
import static org.neo4j.neode.properties.Property.indexableProperty;
import static org.neo4j.neode.properties.Property.property;
import static org.neo4j.neode.properties.PropertyValueSetter.integerRange;

import java.util.List;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.NodeSpecification;
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


        NodeCollection roots = NodeSpecification.createNodes( root )
                .quantity( 10 )
                .update( dataset );

        List<NodeCollection> subnodes = relateNodes( roots ).to(
                randomChoice(
                        getOrCreate( intermediate, 20 )
                                .relationship( "CONNECTED_TO",
                                        property( "quantity", integerRange( 1, 5 ) ) )
                                .relationshipConstraints( minMax( 1, 3 ) ),
                        getOrCreate( leaf, 100 )
                                .relationship( "CONNECTED_TO",
                                        property( "quantity", integerRange( 1, 5 ) ) )
                                .relationshipConstraints( minMax( 1, 3 ) ) )
        ).update( dataset );

        for ( NodeCollection subnode : subnodes )
        {
            if ( subnode.label().equals( "intermediate" ) )
            {
                relateNodes( subnode )
                        .to( getOrCreate( leaf, 100 )
                                .relationship( "CONNECTED_TO",
                                        property( "quantity", integerRange( 1, 5 ) ) )
                                .relationshipConstraints( minMax( 1, 3 ) ) )
                        .update( dataset );
            }
        }

        dataset.end();

        db.shutdown();

    }

}
