/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;

public interface BatchCommandSelector
{
    BatchCommand getNextCommand( Node firstNode );
    Iterable<BatchCommand<DomainEntityInfo>> commands();
}
