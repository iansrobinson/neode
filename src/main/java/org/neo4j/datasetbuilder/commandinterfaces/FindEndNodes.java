package org.neo4j.datasetbuilder.commandinterfaces;

import org.neo4j.datasetbuilder.NodeFinder;

public interface FindEndNodes
{
    Execute findEndNodesUsing( NodeFinder finder );
}
