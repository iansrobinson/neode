package org.neo4j.neode;

import static org.neo4j.neode.Range.minMax;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

class GetExistingUniqueNodes extends Nodes
{
    private final NodeCollection nodeCollection;
    private final ProbabilityDistribution probabilityDistribution;

    GetExistingUniqueNodes( NodeCollection nodeCollection, ProbabilityDistribution probabilityDistribution )
    {
        this.nodeCollection = nodeCollection;
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public Iterable<Node> getNodes( int quantity, final GraphDatabaseService db, Node currentNode, Random random )
    {
        final List<Integer> nodeIdIndexes;
        try
        {
            nodeIdIndexes = probabilityDistribution.generateList(
                    quantity,
                    minMax( 0, nodeCollection.size() - 1 ),
                    random );
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
        return new Iterable<Node>()
        {
            @Override
            public Iterator<Node> iterator()
            {
                return new NodeIterator( new NodeIds()
                {
                    @Override
                    public Long getId( int index )
                    {
                        return nodeCollection.getId( index );
                    }
                }, nodeIdIndexes, db );
            }
        };
    }

    @Override
    public String label()
    {
        return nodeCollection.label();
    }

}
