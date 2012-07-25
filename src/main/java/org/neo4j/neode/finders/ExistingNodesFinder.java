package org.neo4j.neode.finders;

import static java.lang.Math.round;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.numbergenerators.Distribution;

class ExistingNodesFinder
{
    private final double proportionOfCandidateNodesToRequiredNodes;

    ExistingNodesFinder( double proportionOfCandidateNodesToRequiredNodes )
    {
        if ( proportionOfCandidateNodesToRequiredNodes < 1.0 )
        {
            throw new IllegalArgumentException(
                    "proportionOfCandidateNodesToRequiredNodes must be greater than or equal to 1.0" );
        }
        this.proportionOfCandidateNodesToRequiredNodes = proportionOfCandidateNodesToRequiredNodes;
    }

    public List<Node> getExistingNodes( GraphQuery graphQuery, Node currentNode, int quantity, Random random )
    {
        List<Node> existingNodes = new ArrayList<Node>( quantity );
        for ( int i = 0; i < quantity; i++ )
        {
            existingNodes.add( null );
        }

        int candidatePoolSize = (int) round( quantity * proportionOfCandidateNodesToRequiredNodes );
        List<Integer> candidatePoolIndexes = Distribution.flatDistribution()
                .generate( candidatePoolSize, 0, candidatePoolSize - 1, random );

        Iterator<Integer> candidatePoolIndexesIterator = candidatePoolIndexes.iterator();
        Iterator<Node> existingNodesIterator = graphQuery.execute( currentNode ).iterator();

        while ( existingNodesIterator.hasNext() && candidatePoolIndexesIterator.hasNext() )
        {
            Integer nextExistingNodeIndex = candidatePoolIndexesIterator.next();
            if ( nextExistingNodeIndex < quantity )
            {
                existingNodes.set( nextExistingNodeIndex, existingNodesIterator.next() );
            }
        }
        return existingNodes;
    }

}
