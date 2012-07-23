/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder.finders;

import org.neo4j.graphdb.Node;

public interface Query
{
    Iterable<Node> execute(Node startNode);
}
