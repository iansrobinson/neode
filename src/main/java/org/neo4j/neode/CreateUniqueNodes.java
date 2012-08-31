package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class CreateUniqueNodes extends RelationshipBuilder
{
    private final NodeSpecification nodeSpecification;

    CreateUniqueNodes( NodeSpecification nodeSpecification )
    {
        this.nodeSpecification = nodeSpecification;
    }

    @Override
    public Iterable<Node> getNodes( final int quantity, GraphDatabaseService db, Node currentNode, Random random )
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
