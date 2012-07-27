/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;


import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.normalDistribution;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;

public abstract class NodeFinder
{
    public static NodeFinder getExisting( DomainEntityInfo domainEntities, ProbabilityDistribution
            probabilityDistribution )
    {
        return new ExistingUniqueNodeFinder( domainEntities, probabilityDistribution );
    }

    public static NodeFinder getExisting( DomainEntityInfo domainEntities )
    {
        return new ExistingUniqueNodeFinder( domainEntities, normalDistribution() );
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

    public static NodeFinder getOrCreate( DomainEntity domainEntity, int maxNumberOfEntities,
                                          ProbabilityDistribution probabilityDistribution )
    {
        return new GetOrCreateUniqueNodeFinder( domainEntity, maxNumberOfEntities, probabilityDistribution );
    }

    public static NodeFinder getOrCreate( DomainEntity domainEntity, int maxNumberOfEntities )
    {
        return new GetOrCreateUniqueNodeFinder( domainEntity, maxNumberOfEntities, flatDistribution() );
    }

    abstract Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode,
                                      Random random );

    abstract String entityName();
}
