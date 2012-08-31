package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.probabilities.ProbabilityDistribution;
import org.neo4j.neode.test.Db;

public class GetOrCreateUniqueNodesTest
{
    @Test
    public void shouldCreateNewEntitiesWhenTheyDoNotCurrentlyExist() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node currentNode = db.createNode();
        Node newNode = db.createNode();

        ProbabilityDistribution probabilityDistribution = mock( ProbabilityDistribution.class );
        when( probabilityDistribution.generateList( 1, Range.minMax( 1, 5 ) ) ).thenReturn( asList( 1 ) );

        NodeSpecification user = mock( NodeSpecification.class );
        when( user.emptyNodeCollection( 5 ) ).thenReturn(  new NodeCollection(db, new NodeIdCollection( "user", 5 )) );
        when( user.build( 0 ) ).thenReturn( newNode );

        GetOrCreateUniqueNodes nodeFinder = new GetOrCreateUniqueNodes( user, 5, probabilityDistribution );

        // when
        nodeFinder.getTargetNodes( 1, currentNode, db );
        tx.success();
        tx.finish();

        // then
        verify( user ).build( 0 );
    }

    @Test
    public void shouldReuseExistingNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node currentNode = db.createNode();
        Node newNode = db.createNode();

        ProbabilityDistribution probabilityDistribution = mock( ProbabilityDistribution.class );
        when( probabilityDistribution.generateList( 1, Range.minMax( 1, 5 ) ) ).thenReturn( asList( 1 ) );

        NodeSpecification user = mock( NodeSpecification.class );
        when( user.emptyNodeCollection( 5 ) ).thenReturn(  new NodeCollection(db, new NodeIdCollection( "user", 5 )) );
        when( user.build( 0 ) ).thenReturn( newNode );

        GetOrCreateUniqueNodes nodeFinder = new GetOrCreateUniqueNodes( user, 5, probabilityDistribution );

        // when
        nodeFinder.getTargetNodes( 1, currentNode, db );
        nodeFinder.getTargetNodes( 1, currentNode, db );
        tx.success();
        tx.finish();

        // then
        verify( user, times( 1 ) ).build( 0 );
    }
}
