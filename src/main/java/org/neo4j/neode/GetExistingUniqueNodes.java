package org.neo4j.neode;

import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

import static org.neo4j.neode.Range.minMax;

class GetExistingUniqueNodes implements TargetNodesSource
{
    private final NodeCollection nodeCollection;
    private final ProbabilityDistribution probabilityDistribution;

    GetExistingUniqueNodes( NodeCollection nodeCollection, ProbabilityDistribution probabilityDistribution )
    {
        this.nodeCollection = nodeCollection;
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public Iterable<Node> getTargetNodes( int quantity, Node currentNode )
    {
        final List<Integer> nodeIdPositions;
        try
        {
            nodeIdPositions = probabilityDistribution.generateList(
                    quantity,
                    minMax( 0, nodeCollection.size() - 1 )
            );
        }
        catch ( IllegalArgumentException e )
        {
            throw new IllegalStateException( String.format( "Get existing '%s' nodes command cannot complete " +
                    "because the maximum number of nodes available is smaller than the number of " +
                    "nodes specified when applying the relationship constraint. Number of nodes specified by " +
                    "relationship constraint: %s. Maximum number of nodes available: %s. Either adjust the " +
                    "relationship constraint or increase the number of nodes available.",
                    nodeCollection.label(), quantity, nodeCollection.size() ) );
        }
        return nodeCollection.subset( nodeIdPositions );
    }

    @Override
    public String label()
    {
        return nodeCollection.label();
    }

}
