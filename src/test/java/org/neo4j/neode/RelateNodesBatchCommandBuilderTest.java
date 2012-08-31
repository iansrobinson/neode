package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Collections;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.test.Db;

public class RelateNodesBatchCommandBuilderTest
{
    @Test
    public void shouldRelateNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Test" );
        NodeIdCollection users = new NodeSpecification( "user", Collections.<Property>emptyList(), db )
                .create( 3 ).update( dataset );
        NodeSpecification product = new NodeSpecification( "product", Collections.<Property>emptyList(), db );
        RelationshipSpecification bought = dsm.relationshipSpecification( "BOUGHT" );
        final NodeIdCollection products = product.create( 3 ).update( dataset );
        RelationshipBuilder relationshipBuilder = new RelationshipBuilder()
        {
            int index = 0;

            @Override
            public Iterable<Node> getTargetNodes( int quantity, GraphDatabaseService db, Node currentNode )
            {
                return asList( db.getNodeById( products.getIdByPosition( index++ ) ) );
            }

            @Override
            public String label()
            {
                return null;
            }
        };

        // when
        users.createRelationshipsTo(
                relationshipBuilder
                        .relationship( bought )
                        .relationshipConstraints( Range.exactly( 1 ) ) )
                .update( dataset );

        // then
        DynamicRelationshipType bought_rel = withName( "BOUGHT" );

        Node product1 = db.getNodeById( 1 );
        assertTrue( product1.hasRelationship( bought_rel, Direction.OUTGOING ) );
        assertEquals( 4l, product1.getSingleRelationship( bought_rel, Direction.OUTGOING ).getEndNode().getId() );

        Node product2 = db.getNodeById( 2 );
        assertTrue( product2.hasRelationship( bought_rel, Direction.OUTGOING ) );
        assertEquals( 5l, product2.getSingleRelationship( bought_rel, Direction.OUTGOING ).getEndNode().getId() );

        Node product3 = db.getNodeById( 3 );
        assertTrue( product3.hasRelationship( bought_rel, Direction.OUTGOING ) );
        assertEquals( 6l, product3.getSingleRelationship( bought_rel, Direction.OUTGOING ).getEndNode().getId() );
    }
}
