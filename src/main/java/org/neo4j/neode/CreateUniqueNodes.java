package org.neo4j.neode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class CreateUniqueNodes implements TargetNodesSource
{
    private final NodeSpecification nodeSpecification;

    CreateUniqueNodes( NodeSpecification nodeSpecification )
    {
        this.nodeSpecification = nodeSpecification;
    }

    @Override
    public Iterable<Node> getTargetNodes( int quantity, GraphDatabaseService db, Node currentNode )
    {
        NodeIdCollection nodeIdCollection = nodeSpecification.emptyNodeIdCollection( quantity );
        for ( int i = 0; i < quantity; i++ )
        {
            nodeIdCollection.add( nodeSpecification.build( i ).getId() );
        }
        return new NodeCollection( db, nodeIdCollection );
    }

    @Override
    public String label()
    {
        return nodeSpecification.label();
    }
}
