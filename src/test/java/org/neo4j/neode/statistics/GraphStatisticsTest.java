package org.neo4j.neode.statistics;

import java.util.Iterator;

import org.junit.Test;

import org.neo4j.graphdb.*;
import org.neo4j.neode.test.Db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class GraphStatisticsTest
{
    @Test
    public void shouldAddNewNodeWithNoRelationships() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Label user = DynamicLabel.label("user");
        try ( Transaction tx = db.beginTx() )
        {
            db.createNode(user);
            tx.success();
        }

        // when
        GraphStatistics graphStatistics = GraphStatistics.create( db, "test db" );

        // then
        try ( Transaction tx = db.beginTx() )
        {
            Iterator<NodeStatistic> iterator = graphStatistics.nodeStatistics().iterator();
            NodeStatistic nodeStatistic = iterator.next();
            assertEquals( user.name(), nodeStatistic.label() );
            assertEquals( 1, nodeStatistic.count() );
            assertFalse( iterator.hasNext() );
            tx.success();
        }
    }

    @Test
    public void shouldAddRelationshipsByDirection() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Label user = DynamicLabel.label("user");
        try ( Transaction tx = db.beginTx() ) {
            Node firstNode = db.createNode( user );
            Node secondNode = db.createNode( user );
            firstNode.createRelationshipTo( secondNode, withName( "FRIEND" ) );
            secondNode.createRelationshipTo( firstNode, withName( "FRIEND" ) );
            tx.success();
        }

        // when
        GraphStatistics graphStatistics = GraphStatistics.create( db, "test db" );

        // then
        NodeStatistic nodeStatistic = graphStatistics.getNodeStatistic(user.name() );

        assertEquals( 2, nodeStatistic.count() );
        assertEquals( 2, nodeStatistic.getRelationshipStatistic( "FRIEND" ).incoming().total() );
        assertEquals( 2, nodeStatistic.getRelationshipStatistic( "FRIEND" ).outgoing().total() );
        assertEquals( 1, nodeStatistic.getRelationshipStatistic( "FRIEND" ).incoming().average() );
        assertEquals( 1, nodeStatistic.getRelationshipStatistic( "FRIEND" ).outgoing().average() );
    }
}
