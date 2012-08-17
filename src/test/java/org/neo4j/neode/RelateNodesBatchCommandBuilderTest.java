package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Random;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.test.Db;

public class RelateNodesBatchCommandBuilderTest
{
    @Test
    public void shouldRelateNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        NodeCollection users = new NodeSpecification( "user" ).create( 3 ).update( dataset );
        NodeSpecification product = new NodeSpecification( "product" );
        final NodeCollection products = product.create( 3 ).update( dataset );
        Nodes nodes = new Nodes()
        {
            int index = 0;

            @Override
            public Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode,
                                            Random random )
            {
                return asList( db.getNodeById( products.nodeIds().get( index++ ) ) );
            }

            @Override
            public String label()
            {
                return null;
            }
        };

        // when
        users.createRelationshipsTo(
                nodes
                        .relationship( withName( "BOUGHT" ) )
                        .relationshipConstraints( Range.exactly( 1 ) ) )
                .update( dataset );

        // then
        DynamicRelationshipType bought = withName( "BOUGHT" );

        Node product1 = db.getNodeById( 1 );
        assertTrue( product1.hasRelationship( bought, Direction.OUTGOING ) );
        assertEquals( 4l, product1.getSingleRelationship( bought, Direction.OUTGOING ).getEndNode().getId() );

        Node product2 = db.getNodeById( 2 );
        assertTrue( product2.hasRelationship( bought, Direction.OUTGOING ) );
        assertEquals( 5l, product2.getSingleRelationship( bought, Direction.OUTGOING ).getEndNode().getId() );

        Node product3 = db.getNodeById( 3 );
        assertTrue( product3.hasRelationship( bought, Direction.OUTGOING ) );
        assertEquals( 6l, product3.getSingleRelationship( bought, Direction.OUTGOING ).getEndNode().getId() );
    }
}
