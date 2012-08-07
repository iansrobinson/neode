package org.neo4j.neode;

import static java.util.Arrays.asList;

public abstract class TargetNodesSpecificationsChoices
{
    public static TargetNodesSpecificationsChoices randomChoice( TargetNodesSpecification... targetNodesSpecifications )
    {
        return new RandomTargetNodesSpecificationsChoices( asList( targetNodesSpecifications ) );
    }

    abstract Commands createCommandSelector( NodeCollection startNodes, int batchSize, NodeIdCollectorFactory
            nodeIdCollectorFactory );
}
