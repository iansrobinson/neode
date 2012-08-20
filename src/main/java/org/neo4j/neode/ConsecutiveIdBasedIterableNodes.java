package org.neo4j.neode;

import java.util.Iterator;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ConsecutiveIdBasedIterableNodes implements Iterable<Node>
{
    private final GraphDatabaseService db;

    private Long startId = 0L;
    private Long endId = -1L;

    public ConsecutiveIdBasedIterableNodes( GraphDatabaseService db )
    {
        this.db = db;
    }

    public void add( Node node )
    {
        if ( endId < startId )
        {
            startId = endId = node.getId();
        }
        else
        {
            if ( node.getId() - endId != 1 )
            {
                throw new IllegalArgumentException(
                        String.format( "Non-consecutive node Id. Expected: %s, Received: %s.",
                                endId + 1, node.getId() ) );
            }
            endId = node.getId();
        }
    }

    @Override
    public Iterator<Node> iterator()
    {
        return new Iterator<Node>()
        {
            private Long currentId = startId;

            @Override
            public boolean hasNext()
            {
                return currentId <= endId;
            }

            @Override
            public Node next()
            {
                return db.getNodeById( currentId++ );
            }

            @Override
            public void remove()
            {
                throw new IllegalStateException();
            }
        };
    }
}
