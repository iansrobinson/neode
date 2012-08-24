/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

interface NodeSource
{
    public String label();
    public Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random );
}
