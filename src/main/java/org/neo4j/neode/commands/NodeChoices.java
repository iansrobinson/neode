package org.neo4j.neode.commands;

import static java.util.Arrays.asList;

import org.neo4j.neode.NodeCollection;

public abstract class NodeChoices
{
    public static NodeChoices randomChoice( RelationshipSpecification... relationshipSpecifications )
    {
        return new RandomNodeChoices( asList( relationshipSpecifications ) );
    }

    abstract Commands createCommandSelector( NodeCollection startNodes, int batchSize );
}
