package org.neo4j.neode.finders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.numbergenerators.Distribution;

class GetOrCreateUniqueNodeFinder extends NodeFinder
{
    private final DomainEntity domainEntity;
    private final int maxNumberOfNodes;
    private final Distribution distribution;
    private final List<Long> nodeIds;

    GetOrCreateUniqueNodeFinder( DomainEntity domainEntity, int maxNumberOfNodes,
                                 Distribution distribution )
    {
        this.domainEntity = domainEntity;
        this.maxNumberOfNodes = maxNumberOfNodes;
        this.distribution = distribution;
        nodeIds = new ArrayList<Long>( maxNumberOfNodes );
        for ( int i = 0; i < maxNumberOfNodes; i++ )
        {
            nodeIds.add( null );
        }
    }

    @Override
    public Iterable<Node> getNodes( final GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        final List<Integer> nodeIdIndexes = distribution.generate( numberOfNodes, 0,
                maxNumberOfNodes - 1, random );
        for ( Integer nodeIdIndex : nodeIdIndexes )
        {
            if ( nodeIds.get( nodeIdIndex ) == null )
            {
                nodeIds.set( nodeIdIndex, domainEntity.build( db, nodeIdIndex, random ) );
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
