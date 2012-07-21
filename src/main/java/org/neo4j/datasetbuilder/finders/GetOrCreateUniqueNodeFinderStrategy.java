package org.neo4j.datasetbuilder.finders;

import static org.neo4j.datasetbuilder.randomnumbers.NormalDistributionUniqueRandomNumberGenerator.normalDistribution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.commands.DomainEntity;
import org.neo4j.datasetbuilder.randomnumbers.RandomNumberGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class GetOrCreateUniqueNodeFinderStrategy implements NodeFinderStrategy
{
    public static NodeFinderStrategy getOrCreate( DomainEntity domainEntity, int maxNumberOfNodes, RandomNumberGenerator randomNumberGenerator )
    {
        return new GetOrCreateUniqueNodeFinderStrategy( domainEntity, maxNumberOfNodes, randomNumberGenerator );
    }

    public static NodeFinderStrategy getOrCreate( DomainEntity domainEntity, int maxNumberOfNodes )
    {
        return new GetOrCreateUniqueNodeFinderStrategy( domainEntity, maxNumberOfNodes, normalDistribution() );
    }

    private final DomainEntity domainEntity;
    private final int maxNumberOfNodes;
    private final RandomNumberGenerator randomNumberGenerator;
    private final List<Long> nodeIds;

    private GetOrCreateUniqueNodeFinderStrategy( DomainEntity domainEntity, int maxNumberOfNodes, RandomNumberGenerator randomNumberGenerator )
    {
        this.domainEntity = domainEntity;
        this.maxNumberOfNodes = maxNumberOfNodes;
        this.randomNumberGenerator = randomNumberGenerator;
        nodeIds = new ArrayList<Long>( maxNumberOfNodes );
        for ( int i = 0; i < maxNumberOfNodes; i++ )
        {
            nodeIds.add( null );
        }
    }

    @Override
    public Iterable<Node> getNodes( final GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        final List<Integer> nodeIdIndexes = randomNumberGenerator.generate( numberOfNodes, 0,
                maxNumberOfNodes - 1, random );
        for ( Integer nodeIdIndex : nodeIdIndexes )
        {
            if ( nodeIds.get( nodeIdIndex ) == null )
            {
                nodeIds.set( nodeIdIndex, domainEntity.build( db, nodeIdIndex ) );
            }

        }
        return new Iterable<Node>()
        {
            @Override
            public Iterator<Node> iterator()
            {
                return new NodeIterator( nodeIds, nodeIdIndexes, db );
            }
        };
    }

    @Override
    public String entityName()
    {
        return domainEntity.entityName();
    }
}
