package org.neo4j.neode.finders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.numbergenerators.NumberGenerator;

public class GetOrCreateUniqueNodeFinder extends NodeFinder
{
    private final DomainEntity domainEntity;
    private final int maxNumberOfNodes;
    private final NumberGenerator numberGenerator;
    private final List<Long> nodeIds;

    GetOrCreateUniqueNodeFinder( DomainEntity domainEntity, int maxNumberOfNodes,
                                 NumberGenerator numberGenerator )
    {
        this.domainEntity = domainEntity;
        this.maxNumberOfNodes = maxNumberOfNodes;
        this.numberGenerator = numberGenerator;
        nodeIds = new ArrayList<Long>( maxNumberOfNodes );
        for ( int i = 0; i < maxNumberOfNodes; i++ )
        {
            nodeIds.add( null );
        }
    }

    @Override
    public Iterable<Node> getNodes( final GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        final List<Integer> nodeIdIndexes = numberGenerator.generate( numberOfNodes, 0,
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
