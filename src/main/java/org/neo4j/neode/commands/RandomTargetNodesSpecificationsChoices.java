package org.neo4j.neode.commands;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;

class RandomTargetNodesSpecificationsChoices extends TargetNodesSpecificationsChoices
{
    private final List<TargetNodesSpecification> entitiesList;

    public RandomTargetNodesSpecificationsChoices( List<TargetNodesSpecification> entitiesList )
    {
        this.entitiesList = entitiesList;
    }

    @Override
    Commands createCommandSelector( NodeCollection startNodes, int batchSize )
    {
        List<BatchCommand<NodeCollection>> commands = new ArrayList<BatchCommand<NodeCollection>>();
        for ( TargetNodesSpecification targetNodesSpecification : entitiesList )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand( startNodes,
                    targetNodesSpecification, new UniqueNodeIdCollector(), batchSize );
            commands.add( command );
        }
        return new Commands( commands, new RandomCommandSelectionStrategy( ProbabilityDistribution.flatDistribution() ) );
    }


}
