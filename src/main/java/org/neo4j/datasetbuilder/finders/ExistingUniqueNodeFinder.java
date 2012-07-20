package org.neo4j.datasetbuilder.finders;

import java.util.Iterator;
import java.util.List;

import org.neo4j.datasetbuilder.randomnumbers.RandomNumberGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ExistingUniqueNodeFinder implements NodeFinder
{
    private final List<Long> nodeIds;
    private final RandomNumberGenerator generator;

    public ExistingUniqueNodeFinder( List<Long> nodeIds, RandomNumberGenerator generator )
    {
        this.nodeIds = nodeIds;
        this.generator = generator;
    }

    @Override
    public Iterable<Node> getNodes( final GraphDatabaseService db, int numberOfNodes )
    {
        final List<Integer> indexes = generator.generate( numberOfNodes, 0, nodeIds.size() - 1 );
        return new Iterable<Node>()
        {
            @Override
            public Iterator<Node> iterator()
            {
                return new NodeIterator( nodeIds, indexes, db );
            }
        };
    }

    private class NodeIterator implements Iterator<Node>
    {
        private final List<Long> nodeIds;
        private final List<Integer> indexes;
        private final GraphDatabaseService db;
        private int i = 0;

        private NodeIterator( List<Long> nodeIds, List<Integer> indexes, GraphDatabaseService db )
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
            throw new IllegalStateException(  );
        }
    }

}
