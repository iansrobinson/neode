package org.neo4j.neode.commands;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;

class RandomNodeChoices extends NodeChoices
{
    private final List<RelationshipSpecification> entitiesList;

    public RandomNodeChoices( List<RelationshipSpecification> entitiesList )
    {
        this.entitiesList = entitiesList;
    }

    @Override
    Commands createCommandSelector( NodeCollection startNodes, int batchSize )
    {
        List<BatchCommand<NodeCollection>> commands = new ArrayList<BatchCommand<NodeCollection>>();
        for ( RelationshipSpecification relationshipSpecification : entitiesList )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand( startNodes,
                    relationshipSpecification, new UniqueNodeIdCollector(), batchSize );
            commands.add( command );
        }
        return new Commands( commands, new RandomCommandSelectionStrategy( ProbabilityDistribution.flatDistribution() ) );
    }


}
