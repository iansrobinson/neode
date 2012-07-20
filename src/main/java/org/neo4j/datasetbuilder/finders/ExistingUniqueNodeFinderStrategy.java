package org.neo4j.datasetbuilder.finders;

import static org.neo4j.datasetbuilder.randomnumbers.NormalDistributionUniqueRandomNumberGenerator.normalDistribution;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.randomnumbers.RandomNumberGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ExistingUniqueNodeFinderStrategy implements NodeFinderStrategy
{
    public static NodeFinderStrategy getExisting( DomainEntityInfo domainEntities,
                                                  RandomNumberGenerator randomNumberGenerator )
    {
        return new ExistingUniqueNodeFinderStrategy( domainEntities, randomNumberGenerator );
    }

    public static NodeFinderStrategy getExisting( DomainEntityInfo domainEntities )
    {
        return new ExistingUniqueNodeFinderStrategy( domainEntities, normalDistribution() );
    }

    private final DomainEntityInfo domainEntityInfo;
    private final RandomNumberGenerator randomNumberGenerator;

    private ExistingUniqueNodeFinderStrategy( DomainEntityInfo domainEntityInfo, RandomNumberGenerator randomNumberGenerator )
    {
        this.domainEntityInfo = domainEntityInfo;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public Iterable<Node> getNodes( final GraphDatabaseService db, int numberOfNodes,
                                    Random random )
    {
        final List<Integer> indexes = randomNumberGenerator.generate( numberOfNodes, 0,
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
