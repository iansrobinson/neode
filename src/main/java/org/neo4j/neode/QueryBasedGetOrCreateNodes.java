package org.neo4j.neode;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

class QueryBasedGetOrCreateNodes extends RelationshipBuilder
{
    private final NodeSpecification nodeSpecification;
    private final SparseNodeListGenerator sparseNodeListGenerator;

    QueryBasedGetOrCreateNodes( NodeSpecification nodeSpecification, SparseNodeListGenerator sparseNodeListGenerator )
    {
        this.nodeSpecification = nodeSpecification;
        this.sparseNodeListGenerator = sparseNodeListGenerator;
    }

    @Override
    public Iterable<Node> getTargetNodes( int quantity, GraphDatabaseService db, Node currentNode )
    {
        List<Node> nodes = sparseNodeListGenerator.getSparseListOfExistingNodes( quantity, currentNode );
        fillGapsInListWithNewNodes( nodes );

        return nodes;
    }

    private void fillGapsInListWithNewNodes( List<Node> returnNodes )
    {
        for ( int currentNodeIndex = 0; currentNodeIndex < returnNodes.size(); currentNodeIndex++ )
        {
            if ( returnNodes.get( currentNodeIndex ) == null )
            {
                Node newNode = nodeSpecification.build( currentNodeIndex );
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
