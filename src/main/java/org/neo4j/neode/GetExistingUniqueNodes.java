package org.neo4j.neode;

import static org.neo4j.neode.Range.minMax;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

class GetExistingUniqueNodes extends RelationshipBuilder
{
    private final NodeIdCollection nodeIdCollection;
    private final ProbabilityDistribution probabilityDistribution;

    GetExistingUniqueNodes( NodeIdCollection nodeIdCollection, ProbabilityDistribution probabilityDistribution )
    {
        this.nodeIdCollection = nodeIdCollection;
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        final List<Integer> nodeIdPositions;
        try
        {
            nodeIdPositions = probabilityDistribution.generateList(
                    quantity,
                    minMax( 0, nodeIdCollection.size() - 1 ),
                    random );
        }
        catch ( IllegalArgumentException e )
        {
            throw new IllegalStateException( String.format( "Get existing '%s' nodes command cannot complete " +
                    "because the maximum number of nodes available is smaller than the number of " +
                    "nodes specified when applying the relationship constraint. Number of nodes specified by " +
                    "relationship constraint: %s. Maximum number of nodes available: %s. Either adjust the " +
                    "relationship constraint or increase the number of nodes available.",
                    nodeIdCollection.label(), quantity, nodeIdCollection.size() ) );
        }
        return new NodeCollection(  db, nodeIdCollection.subset( nodeIdPositions ));
    }

    @Override
    public String label()
    {
        return nodeIdCollection.label();
    }

}
