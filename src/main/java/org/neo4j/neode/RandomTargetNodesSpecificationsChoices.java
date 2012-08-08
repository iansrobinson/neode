package org.neo4j.neode;

import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;

import java.util.List;

class RandomTargetNodesSpecificationsChoices extends TargetNodesSpecificationsChoices
{
    protected RandomTargetNodesSpecificationsChoices( List<TargetNodesSpecification> targetNodesSpecifications )
    {
        super( targetNodesSpecifications );
    }

    @Override
    protected Commands doCreateCommandSelector( List<BatchCommand<NodeCollection>> commands )
    {
        return new Commands( commands, new RandomCommandSelectionStrategy( flatDistribution() ) );
    }
}
