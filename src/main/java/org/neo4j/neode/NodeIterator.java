package org.neo4j.neode;

import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class NodeIterator implements Iterator<Node>
{
    private final NodeIds nodeIds;
    private final List<Integer> nodeIdIndexes;
    private final GraphDatabaseService db;
    private int i = 0;

    NodeIterator( NodeIds nodeIds, List<Integer> nodeIdIndexes, GraphDatabaseService db )
    {
        this.nodeIds = nodeIds;
        this.nodeIdIndexes = nodeIdIndexes;
        this.db = db;
    }

    @Override
    public boolean hasNext()
    {
        return i < nodeIdIndexes.size();
    }

    @Override
    public Node next()
    {
        return db.getNodeById( nodeIds.getId( nodeIdIndexes.get( i++ ) ) );
    }

    @Override
    public void remove()
    {
        throw new IllegalStateException();
    }
}
