package org.neo4j.neode;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import org.neo4j.graphdb.*;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.test.Db;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryBasedGetOrCreateNodesTest
{
    @Test
    public void shouldReturnAMixtureOfExistingAndNewNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Label label = DynamicLabel.label("user");

        Node node0, node4;
        Iterable<Node> results;
        try ( Transaction tx = db.beginTx() )
        {
            Node currentNode = db.createNode();
            node0 = db.createNode();
            node4 = db.createNode();

            NodeSpecification user = new NodeSpecification( label, Collections.<Property>emptyList(), db );
            SparseNodeListGenerator finder = mock( SparseNodeListGenerator.class );

            List<Node> sparseList = asList( node0, null, null, null, node4 );

            when( finder.getSparseListOfExistingNodes( 5, currentNode ) ).thenReturn( sparseList );

            QueryBasedGetOrCreateNodes queryBasedGetOrCreate = new QueryBasedGetOrCreateNodes( user, finder );

            // when
            results = queryBasedGetOrCreate.getTargetNodes( 5, currentNode );
            tx.success();
        }

        // then
        try ( Transaction tx = db.beginTx() )
        {
            Iterator<Node> iterator = results.iterator();

            assertEquals( node0, iterator.next() );
            assertEquals( true, iterator.next().hasLabel( label ) );
            assertEquals( true, iterator.next().hasLabel( label ) );
            assertEquals( true, iterator.next().hasLabel( label ) );
            assertEquals( node4, iterator.next() );
            assertFalse( iterator.hasNext() );
            tx.success();
        }
    }
}
