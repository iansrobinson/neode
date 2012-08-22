/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import org.neo4j.graphdb.Node;

public interface NodeProvider
{
    public Node getNode( int position );

    public int size();

    public String label();
}
