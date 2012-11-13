package org.neo4j.neode.test;

import static org.neo4j.neode.Range.minMax;
import static org.neo4j.neode.TargetNodesStrategy.create;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.RelationshipSpecification;
import org.neo4j.neode.logging.SysOutLog;

public class SimpleHierarchy
{
    @Test
    public void buildHierarchy()
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Hierarchy" );

        NodeSpecification entity = dsm.nodeSpecification( "entity" );
        RelationshipSpecification child = dsm.relationshipSpecification( "CHILD" );

        NodeCollection root = entity.create( 1 ).update( dataset );
        NodeCollection children = root.createRelationshipsTo(
                create( entity )
                        .numberOfTargetNodes( 3 )
                        .relationship( child )
                        .exactlyOneRelationship() )
                .update( dataset );
        children.createRelationshipsTo(
                create( entity )
                        .numberOfTargetNodes( minMax( 1, 3 ) )
                        .relationship( child )
                        .exactlyOneRelationship() )
                .updateNoReturn( dataset );

        dataset.end();
        db.shutdown();

    }
}
