package org.neo4j.neode.commands;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.Distribution;

class RandomEntityChoices extends EntityChoices
{
    private final List<RelationshipDescription> entitiesList;

    public RandomEntityChoices( List<RelationshipDescription> entitiesList )
    {
        this.entitiesList = entitiesList;
    }

    @Override
    Commands createCommandSelector( DomainEntityInfo startNodes, int batchSize )
    {
        List<BatchCommand<DomainEntityInfo>> commands = new ArrayList<BatchCommand<DomainEntityInfo>>();
        for ( RelationshipDescription relationshipDescription : entitiesList )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand( startNodes,
                    relationshipDescription, new UniqueNodeIdCollector(), batchSize );
            commands.add( command );
        }
        return new Commands( commands, new RandomCommandSelectionStrategy( Distribution.flatDistribution() ) );
    }


}
