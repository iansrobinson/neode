package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.commands.DomainEntity;
import org.neo4j.datasetbuilder.finders.NodeFinderStrategy;

public interface To
{
    RelationshipName to( DomainEntity domainEntity, NodeFinderStrategy nodeFinderStrategyStrategy );
}
