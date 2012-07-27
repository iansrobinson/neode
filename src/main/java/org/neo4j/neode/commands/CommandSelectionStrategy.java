/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;

public interface CommandSelectionStrategy
{
    BatchCommand<DomainEntityInfo> nextCommand( List<BatchCommand<DomainEntityInfo>> commands, Node currentNode,
                                                Random random );
}
