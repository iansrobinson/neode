package org.neo4j.datasetbuilder;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.neo4j.datasetbuilder.DomainEntityBatchCommandBuilder.createEntity;
import static org.neo4j.datasetbuilder.RelateNodesBatchCommandBuilder.relateNodes;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.List;

import org.junit.Test;
import org.neo4j.datasetbuilder.test.Db;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class RelateNodesBatchCommandBuilderTest
{
    @Test
    public void shouldRelateNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );
        List<Long> userIds = createEntity( "user" ).numberOfIterations( 3 ).execute( executor );
        final List<Long> productIds = createEntity( "product" ).numberOfIterations( 3 ).execute( executor );
        NodeFinder finder = new NodeFinder()
        {
            int index = 0;

            @Override
            public Iterable<Node> getNodes( GraphDatabaseService db, int numberOfNodes )
            {
                return asList( db.getNodeById( productIds.get( index++ ) ) );
            }
        };

        // when
        relateNodes( userIds ).startNodeName( "user" ).endNodeName( "product" ).relationshipName( "BOUGHT" )
                .batchSize( 10 ).minMaxNumberOfRels( 1, 1 )
                .findEndNodesUsing( finder )
                .execute( executor );

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
