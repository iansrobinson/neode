package org.neo4j.neode;

import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;

import java.util.List;

class RandomTargetNodesStrategy extends ChoiceOfTargetNodesStrategy
{
    protected RandomTargetNodesStrategy( List<TargetNodesStrategy>
                                                 targetNodeseStrategies )
    {
        super( targetNodeseStrategies );
    }

    @Override
    protected Commands doCreateCommandSelector( List<BatchCommand<NodeIdCollection>> commands )
    {
        return new Commands( commands, new RandomCommandSelectionStrategy( flatDistribution() ) );
    }
}
