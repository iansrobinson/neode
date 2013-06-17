package org.neo4j.neode;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;

import static java.util.Arrays.asList;

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

    Commands createCommandSelector( GraphDatabaseService db, NodeCollection sourceNodes, int batchSize,
                                    NodeCollectionFactory nodeCollectionFactory )
    {
        List<BatchCommand<NodeCollection>> commands = new ArrayList<>();
        for ( TargetNodesStrategy targetNodeStrategy : targetNodeStrategies )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodes, targetNodeStrategy,
                    targetNodeStrategy.newNodeCollection( db, nodeCollectionFactory ), batchSize );
            commands.add( command );
        }
        return doCreateCommandSelector( commands );
    }

    abstract protected Commands doCreateCommandSelector( List<BatchCommand<NodeCollection>> commands );
}
