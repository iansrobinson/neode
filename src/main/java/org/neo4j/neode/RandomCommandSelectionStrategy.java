package org.neo4j.neode;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

class RandomCommandSelectionStrategy implements CommandSelectionStrategy
{
    private final ProbabilityDistribution probabilityDistribution;

    RandomCommandSelectionStrategy( ProbabilityDistribution probabilityDistribution )
    {
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public BatchCommand<NodeIdCollection> nextCommand( List<BatchCommand<NodeIdCollection>> commands, Node currentNode,
                                                       Random random )
    {
        int index = probabilityDistribution.generateSingle( Range.minMax(0, commands.size() - 1), random );
        return commands.get( index );
    }
}
