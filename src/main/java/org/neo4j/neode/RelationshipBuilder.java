/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;


import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;

import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.interfaces.SetRelationshipConstraints;
import org.neo4j.neode.interfaces.SetRelationshipInfo;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public abstract class RelationshipBuilder implements SetRelationshipInfo, SetRelationshipConstraints, NodeSource
{
    private RelationshipInfo relationshipInfo;
    private RelationshipConstraints relationshipConstraints;

    @Override
    public abstract Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random );

    @Override
    public abstract String label();

    @Override
    public final SetRelationshipConstraints relationship(RelationshipSpecification relationshipSpecification,
                                                         Direction direction)
    {
        relationshipInfo = new RelationshipInfo( relationshipSpecification, direction );
        return this;
    }

    @Override
    public final SetRelationshipConstraints relationship(RelationshipSpecification relationshipSpecification)
    {
        return relationship( relationshipSpecification, Direction.OUTGOING );
    }

    @Override
    public final CreateRelationshipSpecification relationshipConstraints( Range cardinality,
                                                                   ProbabilityDistribution probabilityDistribution,
                                                                   RelationshipUniqueness relationshipUniqueness )
    {
        relationshipConstraints = new RelationshipConstraints(
                cardinality,
                relationshipUniqueness,
                probabilityDistribution );
        return new CreateRelationshipSpecification( this, relationshipInfo, relationshipConstraints );
    }

    @Override
    public final CreateRelationshipSpecification relationshipConstraints( Range cardinality,
                                                                   RelationshipUniqueness relationshipUniqueness )
    {
        return relationshipConstraints( cardinality, flatDistribution(), relationshipUniqueness );
    }

    @Override
    public final CreateRelationshipSpecification relationshipConstraints( Range cardinality,
                                                                   ProbabilityDistribution probabilityDistribution )
    {
        return relationshipConstraints( cardinality, probabilityDistribution, RelationshipUniqueness.ALLOW_MULTIPLE );
    }

    @Override
    public final CreateRelationshipSpecification relationshipConstraints( Range cardinality )
    {
        return relationshipConstraints( cardinality, flatDistribution(), RelationshipUniqueness.ALLOW_MULTIPLE );
    }
}
