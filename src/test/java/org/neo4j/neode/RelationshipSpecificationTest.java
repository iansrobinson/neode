package org.neo4j.neode;

import java.util.Collections;

import org.junit.Test;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.properties.PropertyValueGenerator;
import org.neo4j.neode.test.Db;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.properties.Property.property;

public class RelationshipSpecificationTest
{
    @Test
    public void shouldCreateRelationshipBetweenTwoNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Node startNode, endNode;
        try ( Transaction tx = db.beginTx() )
        {
            startNode = db.createNode();
            endNode = db.createNode();

            RelationshipSpecification specification = new RelationshipSpecification( withName( "FRIEND" ),
                    Collections.<Property>emptyList(), db );

            // when
            specification.createRelationship( startNode, endNode, 0 );
            tx.success();
        }

        // then
        try ( Transaction tx = db.beginTx() )
        {
            assertEquals( endNode, startNode.getSingleRelationship( withName( "FRIEND" ),
                    Direction.OUTGOING ).getEndNode() );
            tx.success();
        }
    }

    @Test
    public void shouldCreatePropertiesForRelationship() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Node startNode;
        try ( Transaction tx = db.beginTx() )
        {
            startNode = db.createNode();
            Node endNode = db.createNode();

            PropertyValueGenerator propertyValueGenerator = new PropertyValueGenerator()
            {
                @Override
                public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int
                        iteration )
                {
                    return "value";
                }
            };
            RelationshipSpecification specification = new RelationshipSpecification( withName( "FRIEND" ),
                    asList( property( "name", propertyValueGenerator ) ), db );

            // when
            specification.createRelationship( startNode, endNode, 0 );
            tx.success();
        }

        // then
        try ( Transaction tx = db.beginTx() )
        {
            assertEquals( "value", startNode.getSingleRelationship( withName( "FRIEND" ),
                    Direction.OUTGOING ).getProperty( "name" ) );
            tx.success();
        }
    }

    @Test
    public void shouldExposeRelationshipLabel() throws Exception
    {
        // given
        RelationshipSpecification specification = new RelationshipSpecification( withName( "FRIEND" ),
                Collections.<Property>emptyList(), Db.impermanentDb() );

        // then
        assertEquals( "FRIEND", specification.label() );
    }
}
