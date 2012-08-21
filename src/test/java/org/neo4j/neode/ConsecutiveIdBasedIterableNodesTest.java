package org.neo4j.neode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.test.Db;

public class ConsecutiveIdBasedIterableNodesTest
{
    @Test
    public void shouldReturnEmptyIteratorWhenThereAreNoIds() throws Exception
    {
        // given
        ConsecutiveIdBasedIterableNodes nodes = new ConsecutiveIdBasedIterableNodes( Db.impermanentDb() );

        // then
        assertFalse( nodes.iterator().hasNext() );
    }

    @Test
    public void shouldReturnNodesInIdOrder() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        ConsecutiveIdBasedIterableNodes nodes = new ConsecutiveIdBasedIterableNodes( db );

        // when
        Transaction tx = db.beginTx();
        nodes.add( db.createNode() );
        nodes.add( db.createNode() );
        nodes.add( db.createNode() );
        tx.success();
        tx.finish();

        Iterator<Node> iterator = nodes.iterator();

        // then
        assertEquals( 1L, iterator.next().getId() );
        assertEquals( 2L, iterator.next().getId() );
        assertEquals( 3L, iterator.next().getId() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void shouldThrowExceptionIfNextNodeIdIsNotConsecutiveWithCurrentEndNodeId() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        ConsecutiveIdBasedIterableNodes nodes = new ConsecutiveIdBasedIterableNodes( db );


        Transaction tx = db.beginTx();
        nodes.add( db.createNode() );
        nodes.add( db.createNode() );

        // when
        db.createNode();

        try
        {
            nodes.add( db.createNode() );
            fail( "Expected IllegalArgumentException" );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "Non-consecutive node Id. Expected: 3, Received: 4.", e.getMessage() );
        }
        finally
        {
            tx.success();
            tx.finish();
        }
    }
}
