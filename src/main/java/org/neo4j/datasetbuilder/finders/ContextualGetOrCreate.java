package org.neo4j.datasetbuilder.finders;

import static java.lang.Math.round;
import static org.neo4j.datasetbuilder.numbergenerators.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.DomainEntity;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ContextualGetOrCreate implements NodeFinderStrategy
{
    public static NodeFinderStrategy contextualGetOrCreate( DomainEntity domainEntity,
                                                            Query query )
    {
        return new ContextualGetOrCreate( domainEntity, query, new ExistingNodesFinder( 1.0 ) );
    }

    public static NodeFinderStrategy contextualGetOrCreate( DomainEntity domainEntity,
                                                            Query query,
                                                            double proportionOfCandidateNodesToRequiredNodes )
    {
        return new ContextualGetOrCreate( domainEntity, query,
                new ExistingNodesFinder( proportionOfCandidateNodesToRequiredNodes ) );
    }

    private final DomainEntity domainEntity;
    private final Query query;
    private final ExistingNodesFinder existingNodesFinder;

    private ContextualGetOrCreate( DomainEntity domainEntity, Query query, ExistingNodesFinder existingNodesFinder )
    {
        this.domainEntity = domainEntity;
        this.query = query;
        this.existingNodesFinder = existingNodesFinder;
    }

    @Override
    public Iterable<Node> getNodes( GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        List<Node> returnNodes = existingNodesFinder.getExistingNodes( query, currentNode, numberOfNodes, random );

        for ( int currentNodeIndex = 0; currentNodeIndex < returnNodes.size(); currentNodeIndex++ )
        {
            if ( returnNodes.get( currentNodeIndex ) == null )
            {
                Long nodeId = domainEntity.build( db, currentNodeIndex );
                returnNodes.set( currentNodeIndex, db.getNodeById( nodeId ) );
            }
        }

        return returnNodes;
    }

    @Override
    public String entityName()
    {
        return domainEntity.entityName();
    }

    private static class ExistingNodesFinder
    {
        private final double proportionOfCandidateNodesToRequiredNodes;

        public ExistingNodesFinder( double proportionOfCandidateNodesToRequiredNodes )
        {
            if ( proportionOfCandidateNodesToRequiredNodes < 1.0 )
            {
                throw new IllegalArgumentException(
                        "proportionOfCandidateNodesToRequiredNodes must be greater than or equal to 1.0" );
            }
            this.proportionOfCandidateNodesToRequiredNodes = proportionOfCandidateNodesToRequiredNodes;
        }

        public List<Node> getExistingNodes( Query query, Node currentNode, int quantity, Random random )
        {
            List<Node> existingNodes = new ArrayList<Node>( quantity );
            for ( int i = 0; i < quantity; i++ )
            {
                existingNodes.add( null );
            }

            int candidatePoolSize = (int) round( quantity * proportionOfCandidateNodesToRequiredNodes );
            List<Integer> candidatePoolIndexes = flatDistribution()
                    .generate( candidatePoolSize, 0, candidatePoolSize - 1, random );

            Iterator<Integer> candidatePoolIndexesIterator = candidatePoolIndexes.iterator();
            Iterator<Node> existingNodesIterator = query.execute( currentNode ).iterator();

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
}
