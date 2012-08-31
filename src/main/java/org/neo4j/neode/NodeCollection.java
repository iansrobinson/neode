package org.neo4j.neode;

import java.util.Iterator;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class NodeCollection implements Iterable<Node>
{
    private final GraphDatabaseService db;
    private final NodeIdCollection nodeIdCollection;

    NodeCollection( GraphDatabaseService db, NodeIdCollection nodeIdCollection )
    {
        this.db = db;
        this.nodeIdCollection = nodeIdCollection;
    }

    public void add( Node node )
    {
        nodeIdCollection.add( node.getId() );
    }

    public Node getNode( int position )
    {
        return db.getNodeById( nodeIdCollection.getIdByPosition( position ) );
    }

    public String label()
    {
        return nodeIdCollection.label();
    }

    public int size()
    {
        return nodeIdCollection.size();
    }

    @Override
    public Iterator<Node> iterator()
    {
        final Iterator<Long> idIterator = nodeIdCollection.iterator();

        return new Iterator<Node>()
        {
            @Override
            public boolean hasNext()
            {
                return idIterator.hasNext();
            }

            @Override
            public Node next()
            {
                Long id = idIterator.next();
                return id != null ? db.getNodeById( id ) : null;
            }

            @Override
            public void remove()
            {
                idIterator.remove();
            }
        };
    }
}
