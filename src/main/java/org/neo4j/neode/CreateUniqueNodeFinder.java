package org.neo4j.neode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class CreateUniqueNodeFinder extends NodeFinder
{
    private final NodeSpecification nodeSpecification;

    CreateUniqueNodeFinder( NodeSpecification nodeSpecification )
    {
        this.nodeSpecification = nodeSpecification;
    }

    @Override
    Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        List<Node> nodes = new ArrayList<Node>( quantity );
        for (int iteration = 0; iteration < quantity; iteration++)
        {
            nodes.add(  nodeSpecification.build( iteration, random ));
        }
        return nodes;
    }

    @Override
    String label()
    {
        return nodeSpecification.label();
    }
}
