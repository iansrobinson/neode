package org.neo4j.neode.commands;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;
import org.neo4j.neode.numbergenerators.Range;
import org.neo4j.neode.test.Db;

public class GetOrCreateUniqueNodeFinderTest
{
    @Test
    public void shouldCreateNewEntitiesWhenTheyDoNotCurrentlyExist() throws Exception
    {
        // given
        Random random = new Random();

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node currentNode = db.createNode();
        Node newNode = db.createNode();

        ProbabilityDistribution probabilityDistribution = mock( ProbabilityDistribution.class );
        when( probabilityDistribution.generateList( 1, Range.minMax( 0, 4 ), random ) ).thenReturn( asList( 0 ) );

        NodeSpecification user = mock( NodeSpecification.class );
        when( user.build( db, 0, random ) ).thenReturn( newNode );

        GetOrCreateUniqueNodeFinder nodeFinder = new GetOrCreateUniqueNodeFinder( user, 5, probabilityDistribution );

        // when
        nodeFinder.getNodes( 1, db, currentNode, random );
        tx.success();
        tx.finish();

        // then
        verify( user ).build( db, 0, random );
    }

    @Test
    public void shouldReuseExistingNodes() throws Exception
    {
        // given
        Random random = new Random();

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node currentNode = db.createNode();
        Node newNode = db.createNode();

        ProbabilityDistribution probabilityDistribution = mock( ProbabilityDistribution.class );
        when( probabilityDistribution.generateList( 1, Range.minMax( 0, 4 ), random ) ).thenReturn( asList( 0 ) );

        NodeSpecification user = mock( NodeSpecification.class );
        when( user.build( db, 0, random ) ).thenReturn( newNode );

        GetOrCreateUniqueNodeFinder nodeFinder = new GetOrCreateUniqueNodeFinder( user, 5, probabilityDistribution );

        // when
        nodeFinder.getNodes( 1, db, currentNode, random );
        nodeFinder.getNodes( 1, db, currentNode, random );
        tx.success();
        tx.finish();

        // then
        verify( user, times( 1 ) ).build( db, 0, random );
    }
}
