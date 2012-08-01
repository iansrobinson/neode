/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.commands.NodeBatchCommandBuilder;
import org.neo4j.neode.commands.RelateNodesBatchCommandBuilder;
import org.neo4j.neode.commands.interfaces.SetQuantity;
import org.neo4j.neode.commands.interfaces.SetRelationshipDescription;

public abstract class NodeSpecification
{
    public static NodeBuilder nodeSpec( String name )
    {
        return new NodeBuilder( name );
    }

    public static SetQuantity createNodes( NodeSpecification nodeSpecification )
    {
        return new NodeBatchCommandBuilder( nodeSpecification );
    }

    public static SetRelationshipDescription relateNodes( NodeCollection nodeCollection )
    {
        return new RelateNodesBatchCommandBuilder( nodeCollection );
    }

    public abstract Node build( GraphDatabaseService db, int index, Random random );

    public abstract String label();
}
