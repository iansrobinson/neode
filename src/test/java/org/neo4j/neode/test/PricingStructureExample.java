package org.neo4j.neode.test;

import static org.neo4j.neode.ChoiceOfTargetNodesStrategy.randomChoice;
import static org.neo4j.neode.Range.minMax;
import static org.neo4j.neode.TargetNodesStrategy.getOrCreate;
import static org.neo4j.neode.properties.Property.indexableProperty;
import static org.neo4j.neode.properties.Property.property;
import static org.neo4j.neode.properties.PropertyValueGenerator.integerRange;

import java.util.List;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.RelationshipSpecification;
import org.neo4j.neode.logging.SysOutLog;

public class PricingStructureExample
{
    @Test
    public void buildPricingStructure()
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Pricing tree" );

        NodeSpecification root = dsm.nodeSpecification( "root", indexableProperty( "name" ) );
        NodeSpecification intermediate = dsm.nodeSpecification( "intermediate" );
        NodeSpecification leaf = dsm.nodeSpecification( "leaf", property( "price", integerRange( 1, 10 ) ) );

        RelationshipSpecification connected_to = dsm.relationshipSpecification( "CONNECTED_TO",
                property( "quantity", integerRange( 1, 5 ) ) );

        NodeCollection roots = root.create( 10 ).update( dataset );

        List<NodeCollection> subnodes = roots.createRelationshipsTo(
                randomChoice(
                        getOrCreate( intermediate, 20 )
                                .relationship( connected_to )
                                .relationshipConstraints( minMax( 1, 3 ) ),
                        getOrCreate( leaf, 100 )
                                .relationship( connected_to )
                                .relationshipConstraints( minMax( 1, 3 ) ) ) )
                .update( dataset );

        for ( NodeCollection subnode : subnodes )
        {
            if ( subnode.label().equals( "intermediate" ) )
            {
                subnode.createRelationshipsTo(
                        getOrCreate( leaf, 100 )
                                .relationship( connected_to )
                                .relationshipConstraints( minMax( 1, 3 ) ) )
                        .updateNoReturn( dataset );

            }
        }

        dataset.end();

        db.shutdown();

    }

}
