package org.neo4j.neode.finders;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.NumberGenerator;

public class ExistingUniqueNodeFinder extends NodeFinder
{
    private final DomainEntityInfo domainEntityInfo;
    private final NumberGenerator numberGenerator;

    ExistingUniqueNodeFinder( DomainEntityInfo domainEntityInfo, NumberGenerator numberGenerator )
    {
        this.domainEntityInfo = domainEntityInfo;
        this.numberGenerator = numberGenerator;
    }

    @Override
    public Iterable<Node> getNodes( final GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        final List<Integer> indexes = numberGenerator.generate( numberOfNodes, 0,
                domainEntityInfo.nodeIds().size() - 1, random );
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
