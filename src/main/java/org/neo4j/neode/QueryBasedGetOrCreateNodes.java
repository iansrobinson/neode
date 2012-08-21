package org.neo4j.neode;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class QueryBasedGetOrCreateNodes extends Nodes
{
    private final NodeSpecification nodeSpecification;
    private final SparseNodeListGenerator sparseNodeListGenerator;

    QueryBasedGetOrCreateNodes( NodeSpecification nodeSpecification, SparseNodeListGenerator sparseNodeListGenerator )
    {
        this.nodeSpecification = nodeSpecification;
        this.sparseNodeListGenerator = sparseNodeListGenerator;
    }

    @Override
    public Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        List<Node> nodes = sparseNodeListGenerator.getSparseListOfExistingNodes( quantity, db, currentNode, random );
        fillGapsInListWithNewNodes( db, nodes, random );

        return nodes;
    }

    private void fillGapsInListWithNewNodes( GraphDatabaseService db, List<Node> returnNodes, Random random )
    {
        for ( int currentNodeIndex = 0; currentNodeIndex < returnNodes.size(); currentNodeIndex++ )
        {
            if ( returnNodes.get( currentNodeIndex ) == null )
            {
                Node newNode = nodeSpecification.build( db, currentNodeIndex, random );
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
