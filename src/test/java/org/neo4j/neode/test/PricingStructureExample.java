package org.neo4j.neode.test;

import java.util.List;

import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.RelationshipSpecification;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.statistics.AsciiDocFormatter;
import org.neo4j.neode.statistics.GraphStatistics;

import static org.neo4j.neode.ChoiceOfTargetNodesStrategy.randomChoice;
import static org.neo4j.neode.Range.minMax;
import static org.neo4j.neode.TargetNodesStrategy.getOrCreate;
import static org.neo4j.neode.properties.Property.indexableProperty;
import static org.neo4j.neode.properties.Property.property;
import static org.neo4j.neode.properties.PropertyValueGenerator.integerRange;

public class PricingStructureExample
{
    @Test
    public void buildPricingStructure()
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Pricing tree" );

        NodeSpecification root = dsm.nodeSpecification( "root", indexableProperty( db, "root", "name" ) );
        NodeSpecification intermediate = dsm.nodeSpecification( "intermediate" );
        NodeSpecification leaf = dsm.nodeSpecification( "leaf", property( "price", integerRange( 1, 10 ) ) );

        RelationshipSpecification connected_to = dsm.relationshipSpecification( "CONNECTED_TO",
                property( "quantity", integerRange( 1, 5 ) ) );

        NodeCollection roots = root.create( 10 ).update( dataset );

        List<NodeCollection> subnodes = roots.createRelationshipsTo(
                randomChoice(
                        getOrCreate( intermediate, 20 )
                                .numberOfTargetNodes( minMax( 1, 3 ) )
                                .relationship( connected_to )
                                .exactlyOneRelationship(),
                        getOrCreate( leaf, 100 )
                                .numberOfTargetNodes( minMax( 1, 3 ) )
                                .relationship( connected_to )
                                .exactlyOneRelationship() ) )
                .update( dataset );

        for ( NodeCollection subnode : subnodes )
        {
            if ( subnode.labelName().equals( "intermediate" ) )
            {
                subnode.createRelationshipsTo(
                        getOrCreate( leaf, 100 )
                                .numberOfTargetNodes( minMax( 1, 3 ) )
                                .relationship( connected_to )
                                .exactlyOneRelationship() )
                        .updateNoReturn( dataset );

            }
        }

        dataset.end();

        GraphStatistics statistics = GraphStatistics.create( db, "Pricing Structure" );
        new AsciiDocFormatter( SysOutLog.INSTANCE ).describe( statistics );

        db.shutdown();

    }

}
