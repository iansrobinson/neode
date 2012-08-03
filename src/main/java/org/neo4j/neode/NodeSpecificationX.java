/*
* Copyright (C) 2012 Neo Technology
* All rights reserved
*/
package org.neo4j.neode;

import org.neo4j.neode.commands.RelateNodesBatchCommandBuilder;
import org.neo4j.neode.commands.interfaces.SetRelationshipDescription;

public abstract class NodeSpecificationX
{
//    public static NodeBuilder nodeSpec( String name )
//    {
//        return new NodeBuilder( name );
//    }


    public static SetRelationshipDescription relateNodes( NodeCollection nodeCollection )
    {
        return new RelateNodesBatchCommandBuilder( nodeCollection );
    }



    public abstract String label();
}
