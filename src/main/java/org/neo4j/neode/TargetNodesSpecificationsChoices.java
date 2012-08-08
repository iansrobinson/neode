package org.neo4j.neode;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public abstract class TargetNodesSpecificationsChoices
{
    public static TargetNodesSpecificationsChoices randomChoice( TargetNodesSpecification... targetNodesSpecifications )
    {
        return new RandomTargetNodesSpecificationsChoices( asList( targetNodesSpecifications ) );
    }

    private final List<TargetNodesSpecification> targetNodesSpecifications;

    protected TargetNodesSpecificationsChoices( List<TargetNodesSpecification> targetNodesSpecifications )
    {
        this.targetNodesSpecifications = targetNodesSpecifications;
    }

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
        return doCreateCommandSelector( commands );
    }

    abstract protected Commands doCreateCommandSelector( List<BatchCommand<NodeCollection>> commands );
}
