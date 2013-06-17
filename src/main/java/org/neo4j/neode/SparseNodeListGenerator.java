package org.neo4j.neode;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

import static java.lang.Math.round;

import static org.neo4j.neode.Range.minMax;

class SparseNodeListGenerator
{
    private final GraphQuery query;
    private final ProbabilityDistribution probabilityDistribution;
    private final double proportionOfNodesToListSize;

    SparseNodeListGenerator( GraphQuery query, double proportionOfNodesToListSize,
                             ProbabilityDistribution probabilityDistribution )
    {
        if ( proportionOfNodesToListSize < 1.0 )
        {
            throw new IllegalArgumentException(
                    "proportionOfNodesToListSize must be greater than or equal to 1.0" );
        }

        this.query = query;
        this.probabilityDistribution = probabilityDistribution;
        this.proportionOfNodesToListSize = proportionOfNodesToListSize;
    }

    public List<Node> getSparseListOfExistingNodes( int size, Node currentNode )
    {
        List<Node> existingNodes = new ArrayList<Node>( IteratorUtil.asCollection( query.execute( currentNode ) ) );
        List<Node> sparseList = new ArrayList<Node>( size );

        int candidatePoolSize = (int) round( size * proportionOfNodesToListSize );
        List<Integer> candidateIndexes = probabilityDistribution.generateList( candidatePoolSize,
                minMax( 0, candidatePoolSize - 1 ) );

        for ( int i = 0; i < size; i++ )
        {
            int candidateIndex = candidateIndexes.get( i );
            if ( candidateIndex < existingNodes.size() )
            {
                sparseList.add( existingNodes.get( candidateIndex ) );
            }
            else
            {
                sparseList.add( null );
            }
        }

        return sparseList;
    }

}
