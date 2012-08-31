package org.neo4j.neode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.test.Db;

public class RelationshipUniquenessTest
{
    @Test
    public void uniqueSingleDirectionShouldNotCreateNewRelationshipIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        firstNode.createRelationshipTo( secondNode, friend_of );

        RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                Collections.<Property>emptyList() );
        RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.SINGLE_DIRECTION;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode, relationshipInfo, 0 );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void uniqueSingleDirectionShouldCreateNewRelationshipIfRelationshipAlreadyExistsInOppositeDirection()
            throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        secondNode.createRelationshipTo( firstNode, friend_of );

        RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                Collections.<Property>emptyList() );
        RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.SINGLE_DIRECTION;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode, relationshipInfo, 0 );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
        assertNotNull( iterator.next() );
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void uniqueBothDirectionsShouldNotCreateNewRelationshipIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        firstNode.createRelationshipTo( secondNode, friend_of );

        RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                Collections.<Property>emptyList() );
        RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.BOTH_DIRECTIONS;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode, relationshipInfo, 0 );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void uniqueBothDirectionsShouldNotCreateNewRelationshipIfRelationshipAlreadyExistsInOppositeDirection()
            throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        secondNode.createRelationshipTo( firstNode, friend_of );

        RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                Collections.<Property>emptyList() );
        RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.BOTH_DIRECTIONS;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode, relationshipInfo, 0 );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void allowMultipleShouldCreateNewRelationshipEvenIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        firstNode.createRelationshipTo( secondNode, friend_of );

        RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                Collections.<Property>emptyList() );
        RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.ALLOW_MULTIPLE;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode, relationshipInfo, 0 );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
        assertNotNull( iterator.next() );
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void allowMultipleShouldCreateNewRelationshipEvenIfRelationshipAlreadyExistsInOppositeDirection() throws
            Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        secondNode.createRelationshipTo( firstNode, friend_of );

        RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                Collections.<Property>emptyList() );
        RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.ALLOW_MULTIPLE;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode, relationshipInfo, 0 );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
        assertNotNull( iterator.next() );
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }
}
