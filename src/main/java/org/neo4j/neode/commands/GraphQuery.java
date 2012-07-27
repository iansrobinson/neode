/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.TraversalDescription;

public abstract class GraphQuery
{
    public static GraphQuery traversal( TraversalDescription traversalDescription )
    {
        return new TraversalBasedGraphQuery( traversalDescription );
    }

    abstract Iterable<Node> execute(Node startNode);
}
