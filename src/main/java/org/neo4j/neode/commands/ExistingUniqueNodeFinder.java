package org.neo4j.neode.commands;

import static org.neo4j.neode.numbergenerators.Range.minMax;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.Distribution;

class ExistingUniqueNodeFinder extends NodeFinder
{
    private final DomainEntityInfo domainEntityInfo;
    private final Distribution distribution;

    ExistingUniqueNodeFinder( DomainEntityInfo domainEntityInfo, Distribution distribution )
    {
        this.domainEntityInfo = domainEntityInfo;
        this.distribution = distribution;
    }

    @Override
    public Iterable<Node> getNodes( final GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        final List<Integer> indexes = distribution.generate(
                numberOfNodes,
                minMax( 0, domainEntityInfo.nodeIds().size() - 1 ),
                random );
        return new Iterable<Node>()
        {
            @Override
            public Iterator<Node> iterator()
            {
                return new NodeIterator( domainEntityInfo.nodeIds(), indexes, db );
            }
        };
    }

    @Override
    public String entityName()
    {
        return domainEntityInfo.entityName();
    }

}
