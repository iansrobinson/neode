package org.neo4j.datasetbuilder.finders;

import static org.neo4j.datasetbuilder.randomnumbers.NormalDistributionUniqueRandomNumberGenerator.normalDistribution;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.commands.DomainEntityBuilder;
import org.neo4j.datasetbuilder.randomnumbers.RandomNumberGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class ExistingUniqueNodeFinderStrategy implements NodeFinderStrategy
{
    public static NodeFinderStrategy getExisting( DomainEntityInfo domainEntities,
                                                  RandomNumberGenerator randomNumberGenerator )
    {
        return new ExistingUniqueNodeFinderStrategy( domainEntities.nodeIds(), randomNumberGenerator );
    }

    public static NodeFinderStrategy getExisting( DomainEntityInfo domainEntities )
    {
        return new ExistingUniqueNodeFinderStrategy( domainEntities.nodeIds(), normalDistribution() );
    }

    private final List<Long> nodeIds;
    private final RandomNumberGenerator randomNumberGenerator;

    private ExistingUniqueNodeFinderStrategy( List<Long> nodeIds, RandomNumberGenerator randomNumberGenerator )
    {
        this.nodeIds = nodeIds;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public Iterable<Node> getNodes( final GraphDatabaseService db, int numberOfNodes,
                                    DomainEntityBuilder domainEntityBuilder, Random random )
    {
        final List<Integer> indexes = randomNumberGenerator.generate( numberOfNodes, 0, nodeIds.size() - 1, random );
        return new Iterable<Node>()
        {
            @Override
            public Iterator<Node> iterator()
            {
                return new NodeIterator( nodeIds, indexes, db );
            }
        };
    }

}
