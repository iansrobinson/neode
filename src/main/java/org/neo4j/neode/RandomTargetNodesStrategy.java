package org.neo4j.neode;

import java.util.List;

import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;

class RandomTargetNodesStrategy extends ChoiceOfTargetNodesStrategy
{
    protected RandomTargetNodesStrategy( List<TargetNodesStrategy>
                                                 targetNodeseStrategies )
    {
        super( targetNodeseStrategies );
    }

    @Override
    protected Commands doCreateCommandSelector( List<BatchCommand<NodeCollection>> commands )
    {
        return new Commands( commands, new RandomCommandSelectionStrategy( flatDistribution() ) );
    }
}
