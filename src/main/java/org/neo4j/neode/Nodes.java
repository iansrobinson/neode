/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;


import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;

import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.interfaces.SetRelationshipConstraints;
import org.neo4j.neode.interfaces.SetRelationshipInfo;
import org.neo4j.neode.probabilities.ProbabilityDistribution;
import org.neo4j.neode.properties.Property;

public abstract class Nodes implements SetRelationshipInfo, SetRelationshipConstraints
{
    private RelationshipInfo relationshipInfo;
    private RelationshipConstraints relationshipConstraints;

    abstract Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random );

    abstract String label();

    @Override
    public final SetRelationshipConstraints relationship( RelationshipType relationshipType, Direction direction,
                                                          Property... properties )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, direction, properties );
        return this;
    }

    @Override
    public final SetRelationshipConstraints relationship( RelationshipType relationshipType, Property... properties )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, Direction.OUTGOING, properties );
        return this;
    }

    @Override
    public final SetRelationshipConstraints relationship( String relationshipLabel, Direction direction,
                                                          Property... properties )
    {
        return relationship( withName( relationshipLabel ), direction, properties );
    }

    @Override
    public final SetRelationshipConstraints relationship( String relationshipLabel, Property... properties )
    {
        return relationship( withName( relationshipLabel ), properties );
    }

    @Override
    public final TargetNodesSpecification relationshipConstraints( Range cardinality,
                                                                   ProbabilityDistribution probabilityDistribution,
                                                                   RelationshipUniqueness relationshipUniqueness )
    {
        relationshipConstraints = new RelationshipConstraints(
                cardinality,
                relationshipUniqueness,
                probabilityDistribution );
        return new TargetNodesSpecification( this, relationshipInfo, relationshipConstraints );
    }

    @Override
    public final TargetNodesSpecification relationshipConstraints( Range cardinality,
                                                                   RelationshipUniqueness relationshipUniqueness )
    {
        return relationshipConstraints( cardinality, flatDistribution(), relationshipUniqueness );
    }

    @Override
    public final TargetNodesSpecification relationshipConstraints( Range cardinality,
                                                                   ProbabilityDistribution probabilityDistribution )
    {
        return relationshipConstraints( cardinality, probabilityDistribution, RelationshipUniqueness.ALLOW_MULTIPLE );
    }

    @Override
    public final TargetNodesSpecification relationshipConstraints( Range cardinality )
    {
        return relationshipConstraints( cardinality, flatDistribution(), RelationshipUniqueness.ALLOW_MULTIPLE );
    }
}
