package org.neo4j.neode;

import java.util.List;

import org.neo4j.neode.probabilities.ProbabilityDistribution;

class RandomCommandSelectionStrategy implements CommandSelectionStrategy
{
    private final ProbabilityDistribution probabilityDistribution;

    RandomCommandSelectionStrategy( ProbabilityDistribution probabilityDistribution )
    {
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public BatchCommand<NodeCollection> nextCommand( List<BatchCommand<NodeCollection>> commands )
    {
        int index = probabilityDistribution.generateSingle( Range.minMax( 0, commands.size() - 1 ) );
        return commands.get( index );
    }
}
