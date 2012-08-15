package org.neo4j.neode.test;

import static org.neo4j.neode.Range.exactly;
import static org.neo4j.neode.Range.minMax;
import static org.neo4j.neode.TargetNodesSpecification.create;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.logging.SysOutLog;

public class SimpleHierarchy
{
    @Test
    public void buildHierarchy()
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager datasetManager = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = datasetManager.newDataset( "Hierarchy" );

        NodeSpecification entitySpec = datasetManager.newNodeSpecification( "entity" );

        NodeCollection root = entitySpec.create( 1 ).update( dataset );
        NodeCollection children = root.createRelationshipsTo(
                create( entitySpec )
                        .relationship( "CHILD" )
                        .relationshipConstraints( exactly( 3 ) ) )
                .update( dataset );
        children.createRelationshipsTo(
                create( entitySpec )
                        .relationship( "CHILD" )
                        .relationshipConstraints( minMax( 1, 3 ) ) )
                .updateNoReturn( dataset );

        dataset.end();
        db.shutdown();

    }
}
