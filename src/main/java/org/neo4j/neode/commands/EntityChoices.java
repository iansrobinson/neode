package org.neo4j.neode.commands;

import static java.util.Arrays.asList;

import org.neo4j.neode.NodeCollection;

public abstract class EntityChoices
{
    public static EntityChoices randomChoice( RelationshipSpecification... entitiesList )
    {
        return new RandomEntityChoices( asList( entitiesList ) );
    }

    abstract Commands createCommandSelector( NodeCollection startNodes, int batchSize );
}
