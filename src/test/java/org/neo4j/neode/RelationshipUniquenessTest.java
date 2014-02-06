package org.neo4j.neode;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class RelationshipUniquenessTest
{
    @Test
    public void uniqueSingleDirectionShouldNotCreateNewRelationshipIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        Node firstNode;
        try (Transaction tx = db.beginTx())
        {

            firstNode = db.createNode();
            Node secondNode = db.createNode();
            firstNode.createRelationshipTo( secondNode, friend_of );

            RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                    Collections.<Property>emptyList(), db );
            RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

            RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.SINGLE_DIRECTION;

            // when
            relationshipUniqueness.createRelationship( firstNode, secondNode, relationshipInfo, 0 );
            tx.success();
        }

        // then
        try (Transaction tx = db.beginTx())
        {
            Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
            assertNotNull( iterator.next() );
            assertFalse( iterator.hasNext() );
            tx.success();
        }

    }

    @Test
    public void uniqueSingleDirectionShouldCreateNewRelationshipIfRelationshipAlreadyExistsInOppositeDirection()
            throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DynamicRelationshipType friend_of = withName("FRIEND_OF");
        Node firstNode;
        try (Transaction tx = db.beginTx())
        {
            firstNode = db.createNode();
            Node secondNode = db.createNode();
            secondNode.createRelationshipTo( firstNode, friend_of );

            RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                    Collections.<Property>emptyList(), db );
            RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

            RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.SINGLE_DIRECTION;

            // when
            relationshipUniqueness.createRelationship( firstNode, secondNode, relationshipInfo, 0 );
            tx.success();
        }

        // then
        try (Transaction tx = db.beginTx())
        {
            Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
            assertNotNull( iterator.next() );
            assertNotNull( iterator.next() );
            assertFalse( iterator.hasNext() );
            tx.success();
        }
    }

    @Test
    public void uniqueBothDirectionsShouldNotCreateNewRelationshipIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        Node firstNode;
        try (Transaction tx = db.beginTx())
        {
            firstNode = db.createNode();
            Node secondNode = db.createNode();
            firstNode.createRelationshipTo( secondNode, friend_of );

            RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                    Collections.<Property>emptyList(), db );
            RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

            RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.BOTH_DIRECTIONS;

            // when
            relationshipUniqueness.createRelationship( firstNode, secondNode, relationshipInfo, 0 );
            tx.success();
        }

        // then
        try (Transaction tx = db.beginTx())
        {
            Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
            assertNotNull( iterator.next() );
            assertFalse( iterator.hasNext() );
            tx.success();
        }
    }

    @Test
    public void uniqueBothDirectionsShouldNotCreateNewRelationshipIfRelationshipAlreadyExistsInOppositeDirection()
            throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        Node firstNode;
        try (Transaction tx = db.beginTx())
        {
            firstNode = db.createNode();
            Node secondNode = db.createNode();
            secondNode.createRelationshipTo( firstNode, friend_of );

            RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                    Collections.<Property>emptyList(), db );
            RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

            RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.BOTH_DIRECTIONS;

            // when
            relationshipUniqueness.createRelationship( firstNode, secondNode, relationshipInfo, 0 );
            tx.success();
        }
        // then
        try (Transaction tx = db.beginTx()) {
            Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
            assertNotNull( iterator.next() );
            assertFalse( iterator.hasNext() );
            tx.success();
        }
    }

    @Test
    public void allowMultipleShouldCreateNewRelationshipEvenIfRelationshipAlreadyExists() throws Exception
    {
        // given
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        GraphDatabaseService db = Db.impermanentDb();
        Node firstNode;
        try ( Transaction tx = db.beginTx() )
        {
            firstNode = db.createNode();
            Node secondNode = db.createNode();
            firstNode.createRelationshipTo( secondNode, friend_of );

            RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                    Collections.<Property>emptyList(), db );
            RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

            RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.ALLOW_MULTIPLE;

            // when
            relationshipUniqueness.createRelationship( firstNode, secondNode, relationshipInfo, 0 );
            tx.success();
        }

        // then
        try ( Transaction tx = db.beginTx() )
        {
            Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
            assertNotNull( iterator.next() );
            assertNotNull( iterator.next() );
            assertFalse(iterator.hasNext());
            tx.success();
        }
    }

    @Test
    public void allowMultipleShouldCreateNewRelationshipEvenIfRelationshipAlreadyExistsInOppositeDirection() throws
            Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DynamicRelationshipType friend_of = withName("FRIEND_OF");
        Node firstNode = null;
        try ( Transaction tx = db.beginTx() )
        {
            firstNode = db.createNode();
            Node secondNode = db.createNode();
            secondNode.createRelationshipTo( firstNode, friend_of );

            RelationshipSpecification relationshipSpecification = new RelationshipSpecification( friend_of,
                    Collections.<Property>emptyList(), db );
            RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

            RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.ALLOW_MULTIPLE;

            // when
            relationshipUniqueness.createRelationship( firstNode, secondNode, relationshipInfo, 0 );
            tx.success();
        }

        // then
        try ( Transaction tx = db.beginTx() )
        {
            Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
            assertNotNull( iterator.next() );
            assertNotNull( iterator.next() );
            assertFalse( iterator.hasNext() );
            tx.success();
        }
    }
}
