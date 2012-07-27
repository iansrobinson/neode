package org.neo4j.neode.commands;

import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class NodeIterator implements Iterator<Node>
{
    private final List<Long> nodeIds;
    private final List<Integer> indexes;
    private final GraphDatabaseService db;
    private int i = 0;

    NodeIterator( List<Long> nodeIds, List<Integer> indexes, GraphDatabaseService db )
    {
        this.nodeIds = nodeIds;
        this.indexes = indexes;
        this.db = db;
    }

    @Override
    public boolean hasNext()
    {
        return i < indexes.size();
    }

    @Override
    public Node next()
    {
        return db.getNodeById( nodeIds.get( indexes.get( i++ ) ) );
    }

    @Override
    public void remove()
    {
        throw new IllegalStateException();
    }
}
