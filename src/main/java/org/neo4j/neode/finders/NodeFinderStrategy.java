/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.finders;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public interface NodeFinderStrategy
{
    Iterable<Node> getNodes( GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random );
    String entityName();
}
