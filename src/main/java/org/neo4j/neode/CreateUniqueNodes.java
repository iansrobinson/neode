package org.neo4j.neode;

import org.neo4j.graphdb.Node;

class CreateUniqueNodes implements TargetNodesSource
{
    private final NodeSpecification nodeSpecification;

    CreateUniqueNodes( NodeSpecification nodeSpecification )
    {
        this.nodeSpecification = nodeSpecification;
    }

    @Override
    public Iterable<Node> getTargetNodes( int quantity, Node currentNode )
    {
        NodeCollection nodeCollection = nodeSpecification.emptyNodeCollection( quantity );
        for ( int i = 0; i < quantity; i++ )
        {
            nodeCollection.add( nodeSpecification.build( i ) );
        }
        return nodeCollection;
    }

    @Override
    public String label()
    {
        return nodeSpecification.label();
    }
}
