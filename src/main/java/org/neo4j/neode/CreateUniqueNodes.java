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
    Iterable<Node> getNodes( final int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        NodeCollectionNew nodeCollection = nodeSpecification.emptyNodeCollection( quantity );
        for ( int i = 0; i < quantity; i++ )
        {
            nodeCollection.add( nodeSpecification.build( i ) );
        }
        return nodeCollection;
    }

    @Override
    String label()
    {
        return nodeSpecification.label();
    }
}
