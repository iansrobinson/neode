package org.neo4j.neode.commands;

import static org.neo4j.neode.numbergenerators.Range.minMax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;

class GetOrCreateUniqueNodeFinder extends NodeFinder
{
    private final DomainEntity domainEntity;
    private final int totalNumberOfNodes;
    private final ProbabilityDistribution probabilityDistribution;
    private final List<Long> nodeIds;

    GetOrCreateUniqueNodeFinder( DomainEntity domainEntity, int totalNumberOfNodes, ProbabilityDistribution
            probabilityDistribution )
    {
        this.domainEntity = domainEntity;
        this.totalNumberOfNodes = totalNumberOfNodes;
        this.probabilityDistribution = probabilityDistribution;
        nodeIds = new ArrayList<Long>( totalNumberOfNodes );
        for ( int i = 0; i < totalNumberOfNodes; i++ )
        {
            nodeIds.add( null );
        }
    }

    @Override
    public Iterable<Node> getNodes( int quantity, final GraphDatabaseService db, Node currentNode, Random random )
    {
        final List<Integer> nodeIdIndexes;
        try
        {
            nodeIdIndexes = probabilityDistribution.generateList( quantity, minMax( 0, totalNumberOfNodes - 1 ), random );

        }
        catch ( IllegalArgumentException e )
        {
            throw new IllegalStateException( String.format( "Get or create '%s' nodes command cannot complete " +
                    "because the maximum number of nodes to get or create is smaller than the maximum number of " +
                    "nodes specified in the relationship constraint. Maximum number of nodes in relationship " +
                    "constraint: %s. Maximum number of nodes to get or create: %s. Either reduce the range in the " +
                    "relationship constraint or increase the number of nodes to get or create.",
                    domainEntity.entityName(), quantity, totalNumberOfNodes ) );
        }

        for ( Integer nodeIdIndex : nodeIdIndexes )
        {
            if ( nodeIds.get( nodeIdIndex ) == null )
            {
                nodeIds.set( nodeIdIndex, domainEntity.build( db, nodeIdIndex, random ).getId() );
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
