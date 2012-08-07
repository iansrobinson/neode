package org.neo4j.neode;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.neode.probabilities.ProbabilityDistribution;

class RandomTargetNodesSpecificationsChoices extends TargetNodesSpecificationsChoices
{
    private final List<TargetNodesSpecification> targetNodesSpecifications;

    RandomTargetNodesSpecificationsChoices( List<TargetNodesSpecification> targetNodesSpecifications )
    {
        this.targetNodesSpecifications = targetNodesSpecifications;
    }

    @Override
    Commands createCommandSelector( NodeCollection startNodes, int batchSize,
                                    NodeIdCollectorFactory nodeIdCollectorFactory )
    {
        List<BatchCommand<NodeCollection>> commands = new ArrayList<BatchCommand<NodeCollection>>();
        for ( TargetNodesSpecification targetNodesSpecification : targetNodesSpecifications )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand(
                    startNodes, targetNodesSpecification, nodeIdCollectorFactory.createNodeIdCollector(), batchSize );
            commands.add( command );
        }
        return new Commands( commands,
                new RandomCommandSelectionStrategy( ProbabilityDistribution.flatDistribution() ) );
    }


}
