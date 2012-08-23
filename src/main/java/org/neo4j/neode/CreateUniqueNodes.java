package org.neo4j.neode;

import java.util.Iterator;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class CreateUniqueNodes extends Nodes
{
    private final NodeSpecification nodeSpecification;

    CreateUniqueNodes( NodeSpecification nodeSpecification )
    {
        this.nodeSpecification = nodeSpecification;
    }

    @Override
    Iterable<Node> getNodes( final int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        return new Iterable<Node>()
        {
            @Override
            public Iterator<Node> iterator()
            {
                return new Iterator<Node>()
                {
                    int count = 0;

                    @Override
                    public boolean hasNext()
                    {
                        return count < quantity;
                    }

                    @Override
                    public Node next()
                    {
                        return nodeSpecification.build( count++ );
                    }

                    @Override
                    public void remove()
                    {
                        throw new IllegalStateException();
                    }
                };
            }
        };
    }

    @Override
    String label()
    {
        return nodeSpecification.label();
    }
}
