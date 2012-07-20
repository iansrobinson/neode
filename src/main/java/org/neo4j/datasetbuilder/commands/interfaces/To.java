package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.commands.DomainEntityBuilder;
import org.neo4j.datasetbuilder.finders.NodeFinderStrategy;

public interface To
{
    RelationshipName to( DomainEntityBuilder domainEntity, NodeFinderStrategy nodeFinderStrategyStrategy );
}
