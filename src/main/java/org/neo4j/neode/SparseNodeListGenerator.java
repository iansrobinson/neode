package org.neo4j.neode;

import static java.lang.Math.round;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

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

    public List<Node> getSparseListOfExistingNodes( int size, GraphDatabaseService db, Node currentNode, Random random )
    {
        List<Node> sparseList = new ArrayList<Node>( size );
        for ( int i = 0; i < size; i++ )
        {
            sparseList.add( null );
        }

        int candidatePoolSize = (int) round( size * proportionOfNodesToListSize );
        List<Integer> candidatePoolIndexes = probabilityDistribution
                .generateList( candidatePoolSize, Range.minMax( 0, candidatePoolSize - 1 ), random );

        Iterator<Integer> candidatePoolIndexesIterator = candidatePoolIndexes.iterator();
        Iterator<Node> existingNodesIterator = query.execute( db, currentNode ).iterator();

        while ( existingNodesIterator.hasNext() && candidatePoolIndexesIterator.hasNext() )
        {
            Integer nextExistingNodeIndex = candidatePoolIndexesIterator.next();
            if ( nextExistingNodeIndex < size )
            {
                sparseList.set( nextExistingNodeIndex, existingNodesIterator.next() );
            }
        }
        return sparseList;
    }

}
