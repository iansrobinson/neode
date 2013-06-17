package org.neo4j.neode.test;

import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.Range;
import org.neo4j.neode.RelationshipSpecification;
import org.neo4j.neode.TargetNodesStrategy;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.statistics.AsciiDocFormatter;
import org.neo4j.neode.statistics.GraphStatistics;

import static org.neo4j.neode.properties.Property.indexableProperty;
import static org.neo4j.neode.properties.Property.property;
import static org.neo4j.neode.properties.PropertyValueGenerator.integerRange;

public class MultipleRelsPerNodeExample
{
    @Test
    public void buildMultipleRelsPerNode()
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Multiple Rels Per Node" );

        NodeSpecification root = dsm.nodeSpecification( "root", indexableProperty( "name" ) );
        NodeSpecification leaf = dsm.nodeSpecification( "leaf", property( "price", integerRange( 1, 10 ) ) );

        RelationshipSpecification connected_to = dsm.relationshipSpecification( "CONNECTED_TO",
                property( "quantity", integerRange( 1, 5 ) ) );

        NodeCollection roots = root.create( 1 ).update( dataset );

        roots.createRelationshipsTo( TargetNodesStrategy.create( leaf ).numberOfTargetNodes( 2 ).relationship(
                connected_to ).relationshipConstraints( Range.exactly( 2 ) ) ).updateNoReturn( dataset );

        dataset.end();

        GraphStatistics statistics = GraphStatistics.create( db, "Pricing Structure" );
        new AsciiDocFormatter( SysOutLog.INSTANCE ).describe( statistics );

        db.shutdown();

    }
}
