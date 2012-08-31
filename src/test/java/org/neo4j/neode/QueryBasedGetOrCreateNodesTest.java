package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.test.Db;

public class QueryBasedGetOrCreateNodesTest
{
    @Test
    public void shouldReturnAMixtureOfExistingAndNewNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();

        Transaction tx = db.beginTx();
        Node currentNode = db.createNode();
        Node node0 = db.createNode();
        Node node4 = db.createNode();

        NodeSpecification user = new NodeSpecification( "user", Collections.<Property>emptyList(), db );
        SparseNodeListGenerator finder = mock( SparseNodeListGenerator.class );

        List<Node> sparseList = asList( node0, null, null, null, node4 );

        when( finder.getSparseListOfExistingNodes( 5, currentNode ) ).thenReturn( sparseList );

        QueryBasedGetOrCreateNodes queryBasedGetOrCreate = new QueryBasedGetOrCreateNodes( user, finder );

        // when
        Iterable<Node> results = queryBasedGetOrCreate.getTargetNodes( 5, db, currentNode );
        tx.success();
        tx.finish();

        // then
        Iterator<Node> iterator = results.iterator();

        assertEquals( node0, iterator.next() );
        assertEquals( "user", iterator.next().getProperty( "_label" ) );
        assertEquals( "user", iterator.next().getProperty( "_label" ) );
        assertEquals( "user", iterator.next().getProperty( "_label" ) );
        assertEquals( node4, iterator.next() );
        assertFalse( iterator.hasNext() );
    }
}
