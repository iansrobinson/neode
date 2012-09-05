package org.neo4j.neode.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Iterator;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.test.Db;

public class GraphStatisticsTest
{
    @Test
    public void shouldAddNewNodeWithNoRelationships() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();
        node.setProperty( "_label", "user" );
        tx.success();
        tx.finish();

        GraphStatistics graphStatistics = new GraphStatistics( "test" );

        // when
        graphStatistics.add( node );

        // then
        Iterator<NodeStatistic> iterator = graphStatistics.nodeStatistics().iterator();
        NodeStatistic nodeStatistic = iterator.next();
        assertEquals( "user", nodeStatistic.label() );
        assertEquals( 1, nodeStatistic.count() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void shouldAddRelationshipsByDirection() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        firstNode.setProperty( "_label", "user" );
        Node secondNode = db.createNode();
        secondNode.setProperty( "_label", "user" );
        firstNode.createRelationshipTo( secondNode, withName( "FRIEND" ) );
        secondNode.createRelationshipTo( firstNode, withName( "FRIEND" ) );
        tx.success();
        tx.finish();

        GraphStatistics graphStatistics = new GraphStatistics( "test" );

        // when
        graphStatistics.add( firstNode );
        graphStatistics.add( secondNode );

        // then
        NodeStatistic nodeStatistic = graphStatistics.getNodeStatistic( "user" );

        assertEquals( 2, nodeStatistic.count() );
        assertEquals( 2, nodeStatistic.getRelationshipStatistic( "FRIEND" ).incoming().total() );
        assertEquals( 2, nodeStatistic.getRelationshipStatistic( "FRIEND" ).outgoing().total() );
        assertEquals( 1, nodeStatistic.getRelationshipStatistic( "FRIEND" ).incoming().average() );
        assertEquals( 1, nodeStatistic.getRelationshipStatistic( "FRIEND" ).outgoing().average() );
    }
}
