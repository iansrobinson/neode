/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.commands.DomainEntityBatchCommandBuilder;
import org.neo4j.neode.commands.RelateNodesBatchCommandBuilder;
import org.neo4j.neode.commands.interfaces.To;

public abstract class DomainEntity
{
    public static DomainEntityBuilder domainEntity( String name )
    {
        return new DomainEntityBuilder( name );
    }

    public static DomainEntityBatchCommandBuilder createEntities( DomainEntity domainEntity )
    {
        return new DomainEntityBatchCommandBuilder( domainEntity );
    }

    public static To relateEntities( DomainEntityInfo domainEntityInfo )
    {
        return new RelateNodesBatchCommandBuilder( domainEntityInfo );
    }

    public abstract Long build( GraphDatabaseService db, int index );

    public abstract String entityName();
}
