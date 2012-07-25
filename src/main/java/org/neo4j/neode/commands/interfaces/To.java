package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.finders.NodeFinder;

public interface To
{
    RelationshipName to( NodeFinder nodeFinder );
}
