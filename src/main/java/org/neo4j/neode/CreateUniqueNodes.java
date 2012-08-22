package org.neo4j.neode;

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
        ConsecutiveIdBasedIterableNodes nodes = new ConsecutiveIdBasedIterableNodes( db );
        for (int iteration = 0; iteration < quantity; iteration++)
        {
            nodes.add(  nodeSpecification.build( iteration ));
        }
        return nodes;

    }

    @Override
    String label()
    {
        return nodeSpecification.label();
    }
}
