package org.neo4j.neode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Collections;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.test.Db;

public class RelationshipInfoTest
{
    private static final RelationshipSpecification relationshipSpecification =
            new RelationshipSpecification( withName( "FRIEND" ), Collections.<Property>emptyList(), Db.impermanentDb() );

    @Test
    public void shouldReturnDescriptionOfRelationship() throws Exception
    {
        // given
        RelationshipInfo relationshipInfo1 = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );
        RelationshipInfo relationshipInfo2 = new RelationshipInfo( relationshipSpecification, Direction.INCOMING );

        // then
        assertEquals( "-[:FRIEND]->", relationshipInfo1.description() );
        assertEquals( "<-[:FRIEND]-", relationshipInfo2.description() );
    }

    @Test
    public void shouldRelateFirstNodeToSecondNodeWhenDirectionIsOutgoing() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();

        RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.OUTGOING );

        // when
        relationshipInfo.createRelationship( firstNode, secondNode, db, 0 );
        tx.success();
        tx.finish();

        // then
        assertNotNull( firstNode.getSingleRelationship( withName( "FRIEND" ), Direction.OUTGOING ) );
    }

    @Test
    public void shouldRelateSecondNodeToFirstNodeWhenDirectionIsIncoming() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();

        RelationshipInfo relationshipInfo = new RelationshipInfo( relationshipSpecification, Direction.INCOMING );

        // when
        relationshipInfo.createRelationship( firstNode, secondNode, db, 0 );
        tx.success();
        tx.finish();

        // then
        assertNotNull( firstNode.getSingleRelationship( withName( "FRIEND" ), Direction.INCOMING ) );
    }

    @Test
    public void shouldThrowExceptionIfDirectionIsBoth() throws Exception
    {
        try
        {
            // when
            new RelationshipInfo( relationshipSpecification, Direction.BOTH );
            fail( "Expected IllegalArgumentException" );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "Direction must be either INCOMING or OUTGOING.", e.getMessage() );
        }
    }
}
