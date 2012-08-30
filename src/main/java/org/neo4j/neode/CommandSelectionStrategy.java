/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;

interface CommandSelectionStrategy
{
    BatchCommand<NodeIdCollection> nextCommand( List<BatchCommand<NodeIdCollection>> commands, Node currentNode,
                                                Random random );
}
