package org.neo4j.neode;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public abstract class ChoiceOfTargetNodesStrategy
{
    public static ChoiceOfTargetNodesStrategy randomChoice( TargetNodesStrategy... targetNodeStrategies )
    {
        return new RandomTargetNodesStrategy( asList( targetNodeStrategies ) );
    }

    public static ChoiceOfTargetNodesStrategy all( TargetNodesStrategy... targetNodeStrategies )
    {
        return new ChooseAllTargetNodesStrategies( asList( targetNodeStrategies ) );
    }

    private final List<TargetNodesStrategy> targetNodeStrategies;

    protected ChoiceOfTargetNodesStrategy( List<TargetNodesStrategy> targetNodeStrategies )
    {
        this.targetNodeStrategies = targetNodeStrategies;
    }

    Commands createCommandSelector( NodeIdCollection sourceNodeIds, int batchSize,
                                    NodeIdCollectionFactory nodeIdCollectionFactory )
    {
        List<BatchCommand<NodeIdCollection>> commands = new ArrayList<BatchCommand<NodeIdCollection>>();
        for ( TargetNodesStrategy targetNodeStrategy : targetNodeStrategies )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodeIds, targetNodeStrategy,
                    targetNodeStrategy.newNodeIdCollection( nodeIdCollectionFactory ), batchSize );
            commands.add( command );
        }
        return doCreateCommandSelector( commands );
    }

    abstract protected Commands doCreateCommandSelector( List<BatchCommand<NodeIdCollection>> commands );
}
