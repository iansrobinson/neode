package org.neo4j.neode;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public abstract class CreateRelationshipSpecificationChoices
{
    public static CreateRelationshipSpecificationChoices randomChoice( TargetNodes... targetNodes )
    {
        return new RandomCreateRelationshipSpecificationChoices( asList( targetNodes ) );
    }

    public static CreateRelationshipSpecificationChoices all( TargetNodes... targetNodes )
        {
            return new AllCreateRelationshipSpecificationChoices( asList( targetNodes ) );
        }

    private final List<TargetNodes> targetNodes;

    protected CreateRelationshipSpecificationChoices( List<TargetNodes>
                                                              targetNodes )
    {
        this.targetNodes = targetNodes;
    }

    Commands createCommandSelector( NodeIdCollection sourceNodeIds, int batchSize,
                                           NodeIdCollectionFactory nodeIdCollectionFactory )
    {
        List<BatchCommand<NodeIdCollection>> commands = new ArrayList<BatchCommand<NodeIdCollection>>();
        for ( TargetNodes targetNode : targetNodes )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand(
                    sourceNodeIds, targetNode, targetNode.newNodeIdCollection( nodeIdCollectionFactory ), batchSize );
            commands.add( command );
        }
        return doCreateCommandSelector( commands );
    }

    abstract protected Commands doCreateCommandSelector( List<BatchCommand<NodeIdCollection>> commands );
}
