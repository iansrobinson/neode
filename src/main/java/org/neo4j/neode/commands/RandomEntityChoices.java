package org.neo4j.neode.commands;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;

class RandomEntityChoices extends EntityChoices
{
    private final List<RelationshipDescription> entitiesList;

    public RandomEntityChoices( List<RelationshipDescription> entitiesList )
    {
        this.entitiesList = entitiesList;
    }

    @Override
    Commands createCommandSelector( NodeCollection startNodes, int batchSize )
    {
        List<BatchCommand<NodeCollection>> commands = new ArrayList<BatchCommand<NodeCollection>>();
        for ( RelationshipDescription relationshipDescription : entitiesList )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand( startNodes,
                    relationshipDescription, new UniqueNodeIdCollector(), batchSize );
            commands.add( command );
        }
        return new Commands( commands, new RandomCommandSelectionStrategy( ProbabilityDistribution.flatDistribution() ) );
    }


}
