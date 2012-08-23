package org.neo4j.neode;

import static org.neo4j.neode.Range.minMax;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

class GetOrCreateUniqueNodes extends Nodes
{
    private final NodeSpecification nodeSpecification;
    private final int totalNumberOfNodes;
    private final ProbabilityDistribution probabilityDistribution;
    private final NodeCollectionNew nodeCollection;

    GetOrCreateUniqueNodes( NodeSpecification nodeSpecification, int totalNumberOfNodes,
                            ProbabilityDistribution probabilityDistribution )
    {
        this.nodeSpecification = nodeSpecification;
        this.totalNumberOfNodes = totalNumberOfNodes;
        this.probabilityDistribution = probabilityDistribution;
        this.nodeCollection = nodeSpecification.emptyNodeCollection( totalNumberOfNodes );
    }

    @Override
    public Iterable<Node> getNodes( int quantity, final GraphDatabaseService db, Node currentNode, Random random )
    {
        final List<Integer> nodeIdCounters;
        try
        {
            nodeIdCounters = probabilityDistribution.generateList( quantity, minMax( 1, totalNumberOfNodes ), random );
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

        for ( Integer nodeIdCounter : nodeIdCounters )
        {
            if (nodeIdCounter > nodeCollection.size())
            {
                nodeCollection.add( nodeSpecification.build(nodeCollection.size()) );
            }
        }

        return nodeCollection;
    }

    @Override
    public String label()
    {
        return nodeSpecification.label();
    }
}
