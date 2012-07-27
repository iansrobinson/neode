/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;


import static org.neo4j.neode.numbergenerators.Distribution.flatDistribution;

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

    public static NodeFinder queryBasedGetOrCreate( DomainEntity domainEntity, GraphQuery graphQuery )
    {
        return new QueryBasedGetOrCreate( domainEntity,
                new SparseNodeListGenerator( graphQuery, 1.0, flatDistribution() ) );
    }

    public static NodeFinder queryBasedGetOrCreate( DomainEntity domainEntity, GraphQuery graphQuery,
                                                    double proportionOfCandidateNodesToRequiredNodes )
    {
        return new QueryBasedGetOrCreate( domainEntity,
                new SparseNodeListGenerator( graphQuery, proportionOfCandidateNodesToRequiredNodes,
                        flatDistribution() ) );
    }

    public static NodeFinder getOrCreate( DomainEntity domainEntity, int maxNumberOfNodes,
                                          Distribution distribution )
    {
        return new GetOrCreateUniqueNodeFinder( domainEntity, maxNumberOfNodes, distribution );
    }

    public static NodeFinder getOrCreate( DomainEntity domainEntity, int maxNumberOfNodes )
    {
        return new GetOrCreateUniqueNodeFinder( domainEntity, maxNumberOfNodes, Distribution.flatDistribution() );
    }

    abstract Iterable<Node> getNodes( GraphDatabaseService db, Node currentNode,
                                      int numberOfNodes, Random random );

    abstract String entityName();
}
