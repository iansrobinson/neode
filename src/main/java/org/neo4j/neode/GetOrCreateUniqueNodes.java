package org.neo4j.neode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

import static org.neo4j.neode.Range.minMax;

class GetOrCreateUniqueNodes implements TargetNodesSource
{
    private final NodeSpecification nodeSpecification;
    private final int totalNumberOfNodes;
    private final ProbabilityDistribution probabilityDistribution;
    private final ArrayList<Long> nodeIds;

    GetOrCreateUniqueNodes( NodeSpecification nodeSpecification, int totalNumberOfNodes,
                            ProbabilityDistribution probabilityDistribution )
    {
        this.nodeSpecification = nodeSpecification;
        this.totalNumberOfNodes = totalNumberOfNodes;
        this.probabilityDistribution = probabilityDistribution;

        nodeIds = new ArrayList<>( totalNumberOfNodes );
        for ( int i = 0; i < totalNumberOfNodes; i++ )
        {
            nodeIds.add( null );
        }
    }

    @Override
    public Iterable<Node> getTargetNodes( int quantity, Node currentNode )
    {
        final List<Integer> nodeIdPositions;
        try
        {
            nodeIdPositions = probabilityDistribution.generateList( quantity, minMax( 0, totalNumberOfNodes - 1 ) );
        }
        catch ( IllegalArgumentException e )
        {
            throw new IllegalStateException( String.format( "Get or create '%s' nodes command cannot complete " +
                    "because the maximum number of nodes to get or create is smaller than the maximum number of " +
                    "nodes specified in the relationship constraint. Maximum number of nodes in relationship " +
                    "constraint: %s. Maximum number of nodes to get or create: %s. Either reduce the range in the " +
                    "relationship constraint or increase the number of nodes to get or create.",
                    nodeSpecification.label(), quantity, totalNumberOfNodes ) );
        }

        Set<Long> returnNodeIds = new HashSet<>( quantity );

        for ( Integer nodeIdPosition : nodeIdPositions )
        {
            Long returnNodeId = nodeIds.get( nodeIdPosition );
            if ( returnNodeId == null )
            {
                nodeIds.set( nodeIdPosition, returnNodeId = nodeSpecification.build( nodeIdPosition ).getId() );
            }
            returnNodeIds.add( returnNodeId );
        }

        return nodeSpecification.newNodeCollection( returnNodeIds );
    }

    @Override
    public String label()
    {
        return nodeSpecification.label();
    }
}
