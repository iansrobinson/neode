package org.neo4j.neode.commands;

import static java.util.Arrays.asList;

import org.neo4j.neode.NodeCollection;

public abstract class TargetNodesSpecificationsChoices
{
    public static TargetNodesSpecificationsChoices randomChoice( TargetNodesSpecification... targetNodesSpecifications )
    {
        return new RandomTargetNodesSpecificationsChoices( asList( targetNodesSpecifications ) );
    }

    abstract Commands createCommandSelector( NodeCollection startNodes, int batchSize );
}
