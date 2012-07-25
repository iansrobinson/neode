/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.finders;


import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.Distribution;

public abstract class NodeFinder
{
    public static NodeFinder getExisting( DomainEntityInfo domainEntities, Distribution distribution )
    {
        return new ExistingUniqueNodeFinder( domainEntities, distribution );
    }

    public static NodeFinder getExisting( DomainEntityInfo domainEntities )
    {
        return new ExistingUniqueNodeFinder( domainEntities, Distribution.normalDistribution() );
    }

    public static NodeFinder contextualGetOrCreate( DomainEntity domainEntity, GraphQuery graphQuery )
    {
        return new ContextualGetOrCreate( domainEntity, graphQuery, new ExistingNodesFinder( 1.0 ) );
    }

    public static NodeFinder contextualGetOrCreate( DomainEntity domainEntity, GraphQuery graphQuery,
                                                    double proportionOfCandidateNodesToRequiredNodes )
    {
        return new ContextualGetOrCreate( domainEntity, graphQuery,
                new ExistingNodesFinder( proportionOfCandidateNodesToRequiredNodes ) );
    }

    public static NodeFinder getOrCreate( DomainEntity domainEntity, int maxNumberOfNodes,
                                          Distribution distribution )
    {
        return new GetOrCreateUniqueNodeFinder( domainEntity, maxNumberOfNodes, distribution );
    }

    public static NodeFinder getOrCreate( DomainEntity domainEntity, int maxNumberOfNodes )
    {
        return new GetOrCreateUniqueNodeFinder( domainEntity, maxNumberOfNodes, Distribution.normalDistribution() );
    }

    public abstract Iterable<Node> getNodes( GraphDatabaseService db, Node currentNode,
                                             int numberOfNodes, Random random );

    public abstract String entityName();
}
