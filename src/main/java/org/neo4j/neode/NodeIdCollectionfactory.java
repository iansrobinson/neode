/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import java.util.ArrayList;

enum NodeIdCollectionFactory
{
    INSTANCE
            {
                @Override
                NodeIdCollection createNodeIdCollection( String label )
                {
                    return new NodeIdCollection( label, new ArrayList<Long>() );
                }
            },
    NULL
            {
                @Override
                NodeIdCollection createNodeIdCollection( String label )
                {
                    return NodeIdCollection.NULL;
                }
            };

    abstract NodeIdCollection createNodeIdCollection( String label );
}
