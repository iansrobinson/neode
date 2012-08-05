package org.neo4j.neode.commands;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class QueryBasedGetOrCreate extends NodeFinder
{
    private final NodeSpecification nodeSpecification;
    private final SparseNodeListGenerator sparseNodeListGenerator;

    QueryBasedGetOrCreate( NodeSpecification nodeSpecification, SparseNodeListGenerator sparseNodeListGenerator )
    {
        this.nodeSpecification = nodeSpecification;
        this.sparseNodeListGenerator = sparseNodeListGenerator;
    }

    @Override
    public Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        List<Node> nodes = sparseNodeListGenerator.getSparseListOfExistingNodes( quantity, currentNode, random );
        fillGapsInListWithNewNodes( nodes, random );

        return nodes;
    }

    private void fillGapsInListWithNewNodes( List<Node> returnNodes, Random random )
    {
        for ( int currentNodeIndex = 0; currentNodeIndex < returnNodes.size(); currentNodeIndex++ )
        {
            if ( returnNodes.get( currentNodeIndex ) == null )
            {
                Node newNode = nodeSpecification.build( currentNodeIndex, random );
                returnNodes.set( currentNodeIndex, newNode );
            }
        }
    }

    @Override
    public String label()
    {
        return nodeSpecification.label();
    }
}
