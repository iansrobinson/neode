package org.neo4j.datasetbuilder.finders;

import static org.neo4j.datasetbuilder.numbergenerators.NormalDistributionUniqueRandomNumberGenerator.normalDistribution;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.numbergenerators.NumberGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ExistingUniqueNodeFinderStrategy implements NodeFinderStrategy
{
    public static NodeFinderStrategy getExisting( DomainEntityInfo domainEntities,
                                                  NumberGenerator numberGenerator )
    {
        return new ExistingUniqueNodeFinderStrategy( domainEntities, numberGenerator );
    }

    public static NodeFinderStrategy getExisting( DomainEntityInfo domainEntities )
    {
        return new ExistingUniqueNodeFinderStrategy( domainEntities, normalDistribution() );
    }

    private final DomainEntityInfo domainEntityInfo;
    private final NumberGenerator numberGenerator;

    private ExistingUniqueNodeFinderStrategy( DomainEntityInfo domainEntityInfo,
                                              NumberGenerator numberGenerator )
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
