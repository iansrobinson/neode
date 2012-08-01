/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;


import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.normalDistribution;

import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.commands.interfaces.SetRelationshipConstraints;
import org.neo4j.neode.commands.interfaces.SetRelationshipInfo;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;
import org.neo4j.neode.numbergenerators.Range;
import org.neo4j.neode.properties.Property;

public abstract class NodeFinder   implements SetRelationshipInfo, SetRelationshipConstraints
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

    private RelationshipInfo relationshipInfo;
    private RelationshipConstraints relationshipConstraints;

    @Override
    public SetRelationshipConstraints relationship( RelationshipType relationshipType, Direction direction,
                                                        Property... properties )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, direction, properties );
        return this;
    }

    @Override
    public SetRelationshipConstraints relationship( RelationshipType relationshipType, Property... properties )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, Direction.OUTGOING, properties );
        return this;
    }

    @Override
    public RelationshipDescription relationshipConstraints( Range cardinality,
                                                            RelationshipUniqueness relationshipUniqueness )
    {
        relationshipConstraints = new RelationshipConstraints( cardinality, relationshipUniqueness );
        return new RelationshipDescription( this, relationshipInfo, relationshipConstraints );
    }

    @Override
    public RelationshipDescription relationshipConstraints( Range cardinality )
    {
        relationshipConstraints = new RelationshipConstraints( cardinality, RelationshipUniqueness.ALLOW_MULTIPLE );
        return new RelationshipDescription( this, relationshipInfo, relationshipConstraints );
    }
}
