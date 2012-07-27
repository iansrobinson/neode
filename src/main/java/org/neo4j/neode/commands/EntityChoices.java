package org.neo4j.neode.commands;

import static java.util.Arrays.asList;

import org.neo4j.neode.DomainEntityInfo;

public abstract class EntityChoices
{
    public static EntityChoices randomChoice( RelationshipDescription... entitiesList )
    {
        return new RandomEntityChoices( asList( entitiesList ) );
    }

    abstract Commands createCommandSelector( DomainEntityInfo startNodes, int batchSize );
}
