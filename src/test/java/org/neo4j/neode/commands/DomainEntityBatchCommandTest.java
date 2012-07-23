package org.neo4j.neode.commands;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.neo4j.neode.DomainEntityBuilder.domainEntity;
import static org.neo4j.neode.commands.DomainEntityBatchCommandBuilder.createEntities;
import static org.neo4j.neode.properties.Property.indexableProperty;

import org.junit.Test;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.test.Db;
import org.neo4j.graphdb.GraphDatabaseService;

public class DomainEntityBatchCommandTest
{
    @Test
    public void shouldCreateEntityPrefixedWithEntityName() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        DomainEntity user = domainEntity( "user" ).withProperties( indexableProperty( "name" ) ).build();

        // when
        createEntities( user ).quantity( 1 ).addTo( dataset );

        // then
        assertEquals( "user-1", db.getNodeById( 1 ).getProperty( "name" ) );
    }

    @Test
    public void shouldIndexIndexableEntity() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        DomainEntity user = domainEntity( "user" ).withProperties( indexableProperty( "name" ) ).build();

        // when
        createEntities( user ).quantity( 1 ).addTo( dataset );

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
        DomainEntity user = domainEntity( "user" ).withProperties( indexableProperty( "key" ) ).build();

        // when
        DomainEntityInfo results = createEntities( user ).quantity( 5 ).addTo( dataset );

        // then
        assertEquals( asList( 1l, 2l, 3l, 4l, 5l ), results.nodeIds() );
    }
}
