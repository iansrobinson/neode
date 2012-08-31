/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import org.neo4j.graphdb.Node;

interface TargetNodesSource
{
    public String label();
    public Iterable<Node> getTargetNodes( int quantity, Node currentNode );
}
