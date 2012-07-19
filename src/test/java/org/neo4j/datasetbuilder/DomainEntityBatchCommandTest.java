package org.neo4j.datasetbuilder;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.neo4j.datasetbuilder.DomainEntityBatchCommandBuilder.commandFor;

import java.util.List;

import org.junit.Test;
import org.neo4j.datasetbuilder.test.Db;
import org.neo4j.graphdb.GraphDatabaseService;

public class DomainEntityBatchCommandTest
{
    @Test
    public void shouldCreateEntityPrefixedWithEntityName() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        BatchCommand<List<Long>> command = commandFor( "user" ).numberOfIterations( 1 ).batchSize( 1 ).build();
        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );

        // when
        executor.execute( command );

        // then
        assertEquals( "user-0", db.getNodeById( 1 ).getProperty( "name" ) );
    }

    @Test
    public void shouldIndexIndexableEntity() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        BatchCommand<List<Long>> command = commandFor( "user" ).numberOfIterations( 1 ).batchSize( 1 )
                .isIndexable( true ).build();
        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );

        // when
        executor.execute( command );

        // then
        assertNotNull( db.index().forNodes( "user" ).get( "name", "user-0" ).getSingle() );
    }

    @Test
    public void shouldSupplyUserDefinedPropertyName() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        BatchCommand<List<Long>> command = commandFor( "user" ).numberOfIterations( 1 ).batchSize( 1 ).propertyName( "key" )
                .build();
        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );

        // when
        executor.execute( command );

        // then
        assertEquals( "user-0", db.getNodeById( 1 ).getProperty( "key" ) );
    }

    @Test
    public void shouldReturnListOfCreatedNodeIds() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        BatchCommand<List<Long>> command = commandFor( "user" ).numberOfIterations( 5 ).build();
        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );

        // when
        Results<List<Long>> results = executor.execute( command );

        // then
        assertEquals( asList( 1l, 2l, 3l, 4l, 5l ), results.value() );
    }
}
