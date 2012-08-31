package org.neo4j.neode;

import static org.neo4j.neode.Range.minMax;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

class GetOrCreateUniqueNodes extends RelationshipBuilder
{
    private final NodeSpecification nodeSpecification;
    private final int totalNumberOfNodes;
    private final ProbabilityDistribution probabilityDistribution;
    private final NodeIdCollection nodeIdCollection;

    GetOrCreateUniqueNodes( NodeSpecification nodeSpecification, int totalNumberOfNodes,
                            ProbabilityDistribution probabilityDistribution )
    {
        this.nodeSpecification = nodeSpecification;
        this.totalNumberOfNodes = totalNumberOfNodes;
        this.probabilityDistribution = probabilityDistribution;
        this.nodeIdCollection = nodeSpecification.emptyNodeIdCollection( totalNumberOfNodes );
    }

    @Override
    public Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        final List<Integer> nodeIdCounters;
        try
        {
            nodeIdCounters = probabilityDistribution.generateList( quantity, minMax( 1, totalNumberOfNodes ) );
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
            if ( nodeIdCounter > nodeIdCollection.size() )
            {
                nodeIdCollection.add( nodeSpecification.build( nodeIdCollection.size() ).getId() );
            }
        }

        return new NodeCollection( db, nodeIdCollection );
    }

    @Override
    public String label()
    {
        return nodeSpecification.label();
    }
}
