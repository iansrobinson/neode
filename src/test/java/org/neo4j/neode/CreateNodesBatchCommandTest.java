package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.neo4j.neode.properties.Property.indexableProperty;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.test.Db;

public class CreateNodesBatchCommandTest
{
    @Test
    public void shouldCreateNodeWithNamePropertyValuePrefixedWithNodeLabel() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        NodeSpecification user = new NodeSpecification( "user", indexableProperty( "name" ) );

        // when
        user.create( 1 ).update( dataset );

        // then
        assertEquals( "user-1", db.getNodeById( 1 ).getProperty( "name" ) );
    }

    @Test
    public void shouldIndexIndexableNode() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        NodeSpecification user = new NodeSpecification( "user", indexableProperty( "name" ) );

        // when
        user.create( 1 ).update( dataset );

        // then
        assertNotNull( db.index().forNodes( "user" ).get( "name", "user-1" ).getSingle() );
    }

    @Test
    public void shouldReturnListOfCreatedNodeIds() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        NodeSpecification user = new NodeSpecification( "user", indexableProperty( "key" ) );

        // when
        NodeCollection results = user.create( 5 ).update( dataset );

        // then
        assertEquals( asList( 1l, 2l, 3l, 4l, 5l ), results.nodeIds() );
    }
}
