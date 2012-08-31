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
        NodeSpecification user = new NodeSpecification( "user",  asList( indexableProperty( "name" ) ), db );

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
        NodeSpecification user = new NodeSpecification( "user", asList( indexableProperty( "name" ) ), db );

        // when
        user.create( 1 ).update( dataset );

        // then
        assertNotNull( db.index().forNodes( "user" ).get( "name", "user-1" ).getSingle() );
    }

    @Test
    public void shouldReturnCollectionOfCreatedNodeIds() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        NodeSpecification user = new NodeSpecification( "user", asList( indexableProperty( "key" ) ), db );

        // when
        NodeIdCollection results = user.create( 5 ).update( dataset );

        // then
        assertEquals( 5, results.size() );
        assertEquals( (Object) 1L, results.getIdByPosition( 0 ) );
        assertEquals( (Object) 2L, results.getIdByPosition( 1 ) );
        assertEquals( (Object) 3L, results.getIdByPosition( 2 ) );
        assertEquals( (Object) 4L, results.getIdByPosition( 3 ) );
        assertEquals( (Object) 5L, results.getIdByPosition( 4 ) );
    }
}
