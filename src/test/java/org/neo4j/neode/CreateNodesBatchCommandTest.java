package org.neo4j.neode;

import org.junit.Test;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.test.Db;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.neo4j.neode.properties.Property.indexableProperty;

public class CreateNodesBatchCommandTest
{
    @Test
    public void shouldCreateNodeWithNamePropertyValuePrefixedWithNodeLabel() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Test" );
        NodeSpecification user = new NodeSpecification( "user", asList( indexableProperty( db, "user", "name" ) ), db );

        // when
        user.create( 1 ).update( dataset );

        // then
        try ( Transaction tx = db.beginTx() )
        {
            assertEquals( "user-1", db.getNodeById( 0 ).getProperty( "name" ) );
            tx.success();
        }

    }

    @Test
    public void shouldIndexIndexableNode() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Test" );
        NodeSpecification user = new NodeSpecification( "user", asList( indexableProperty( db, "user", "name" ) ), db );

        // when
        user.create( 1 ).update( dataset );

        // then
        try ( Transaction tx = db.beginTx() )
        {
            assertNotNull( IteratorUtil.singleOrNull(db.findNodesByLabelAndProperty(DynamicLabel.label("user"), "name", "user-1")) );
            tx.success();
        }
    }

    @Test
    public void shouldReturnCollectionOfCreatedNodeIds() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Test" );
        NodeSpecification user = new NodeSpecification( "user", asList( indexableProperty( db, "user", "key" ) ), db );

        // when
        NodeCollection results = user.create( 5 ).update( dataset );

        // then
        try ( Transaction tx = db.beginTx() )
        {
            assertEquals( 5, results.size() );
            assertEquals( (Object) 0L, results.getNodeByPosition( 0 ).getId() );
            assertEquals( (Object) 1L, results.getNodeByPosition( 1 ).getId() );
            assertEquals( (Object) 2L, results.getNodeByPosition( 2 ).getId() );
            assertEquals( (Object) 3L, results.getNodeByPosition( 3 ).getId() );
            assertEquals( (Object) 4L, results.getNodeByPosition( 4 ).getId() );
            tx.success();
        }
    }
}
