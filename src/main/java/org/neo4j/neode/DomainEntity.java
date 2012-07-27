/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.commands.DomainEntityBatchCommandBuilder;
import org.neo4j.neode.commands.RelateNodesBatchCommandBuilder;
import org.neo4j.neode.commands.interfaces.SetQuantity;
import org.neo4j.neode.commands.interfaces.SetRelationshipDescription;

public abstract class DomainEntity
{
    public static DomainEntityBuilder domainEntity( String name )
    {
        return new DomainEntityBuilder( name );
    }

    public static SetQuantity createEntities( DomainEntity domainEntity )
    {
        return new DomainEntityBatchCommandBuilder( domainEntity );
    }

    public static SetRelationshipDescription relateEntities( DomainEntityInfo domainEntityInfo )
    {
        return new RelateNodesBatchCommandBuilder( domainEntityInfo );
    }



    public abstract Long build( GraphDatabaseService db, int index, Random random );

    public abstract String entityName();
}
