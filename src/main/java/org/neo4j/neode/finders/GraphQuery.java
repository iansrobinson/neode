/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.finders;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.TraversalDescription;

public abstract class GraphQuery
{
    public static GraphQuery traversal( TraversalDescription traversalDescription )
    {
        return new TraversalBasedGraphQuery( traversalDescription );
    }

    public abstract Iterable<Node> execute(Node startNode);
}
