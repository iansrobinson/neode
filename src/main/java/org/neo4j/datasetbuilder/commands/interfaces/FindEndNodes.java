package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.finders.NodeFinder;

public interface FindEndNodes
{
    Execute findEndNodesUsing( NodeFinder finder );
}
