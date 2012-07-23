package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.finders.NodeFinderStrategy;

public interface To
{
    RelationshipName to( NodeFinderStrategy nodeFinderStrategyStrategy );
}
