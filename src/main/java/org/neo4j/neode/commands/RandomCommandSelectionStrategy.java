package org.neo4j.neode.commands;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;
import org.neo4j.neode.numbergenerators.Range;

public class RandomCommandSelectionStrategy implements CommandSelectionStrategy
{
    private final ProbabilityDistribution probabilityDistribution;

    public RandomCommandSelectionStrategy( ProbabilityDistribution probabilityDistribution )
    {
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public BatchCommand<DomainEntityInfo> nextCommand( List<BatchCommand<DomainEntityInfo>> commands, Node currentNode,
                                                       Random random )
    {
        int index = probabilityDistribution.generateSingle( Range.minMax(0, commands.size() - 1), random );
        return commands.get( index );
    }
}
