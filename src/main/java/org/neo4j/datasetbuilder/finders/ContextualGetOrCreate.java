package org.neo4j.datasetbuilder.finders;

import static org.neo4j.datasetbuilder.numbergenerators.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.DomainEntity;
import org.neo4j.datasetbuilder.numbergenerators.NumberGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ContextualGetOrCreate implements NodeFinderStrategy
{
    public static NodeFinderStrategy contextualGetOrCreate( DomainEntity domainEntity,
                                                            Query query )
    {
        return new ContextualGetOrCreate( domainEntity, query );
    }

    private final DomainEntity domainEntity;
    private final Query query;

    private ContextualGetOrCreate( DomainEntity domainEntity, Query query )
    {
        this.domainEntity = domainEntity;
        this.query = query;
    }

    @Override
    public Iterable<Node> getNodes( GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        List<Node> returnNodes = new ArrayList<Node>( numberOfNodes );
        for ( int i = 0; i < numberOfNodes; i++ )
        {
            returnNodes.add( null );
        }

        NumberGenerator numberGenerator = flatDistribution();

        List<Integer> contextIndexes = numberGenerator.generate( numberOfNodes, 0, numberOfNodes - 1, random );

        Iterator<Integer> contextIndexesIterator = contextIndexes.iterator();
        Iterator<Node> existingNodesIterator = query.execute( currentNode ).iterator();

        while ( existingNodesIterator.hasNext() && contextIndexesIterator.hasNext() )
        {
            returnNodes.set( contextIndexesIterator.next(), existingNodesIterator.next() );
        }

        List<Integer> currentNodeIndexes = numberGenerator.generate( numberOfNodes, 0, numberOfNodes - 1, random );
        for ( Integer currentNodeIndex : currentNodeIndexes )
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
}
