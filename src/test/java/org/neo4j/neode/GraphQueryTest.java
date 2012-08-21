package org.neo4j.neode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Iterator;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser;
import org.neo4j.neode.test.Db;

public class GraphQueryTest
{
    @Test
    public void shouldBeAbleToImplementAnonymousGraphQuery() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        firstNode.createRelationshipTo( secondNode, withName("CONNECTED_TO") );
        tx.success();
        tx.finish();

        GraphQuery query = new GraphQuery()
        {
            @Override
            public Iterable<Node> execute( GraphDatabaseService db, Node startNode )
            {
             return startNode.traverse( Traverser.Order.DEPTH_FIRST, StopEvaluator.DEPTH_ONE,
                     ReturnableEvaluator.ALL_BUT_START_NODE,withName( "CONNECTED_TO"), Direction.OUTGOING );
            }
        };

        // when
        Iterable<Node> result = query.execute( db, firstNode );

        // then
        Iterator<Node> iterator = result.iterator();
        assertEquals(secondNode, iterator.next());
        assertFalse(iterator.hasNext());
    }
}
