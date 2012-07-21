package org.neo4j.datasetbuilder.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Iterator;

import org.junit.Test;
import org.neo4j.datasetbuilder.test.Db;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

public class UniqueTest
{
    @Test
    public void shouldNotCreateNewRelationshipIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        firstNode.createRelationshipTo( secondNode, friend_of );

        UniquenessStrategy strategy = Unique.unique();

        // when
        strategy.apply( db, firstNode, secondNode, friend_of, Direction.OUTGOING );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void shouldCreateNewRelationshipIfRelationshipAlreadyExistsInOppositeDirection() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        secondNode.createRelationshipTo( firstNode, friend_of );

        UniquenessStrategy strategy = Unique.unique();

        // when
        strategy.apply( db, firstNode, secondNode, friend_of, Direction.OUTGOING );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
        assertNotNull( iterator.next() );
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }
}
