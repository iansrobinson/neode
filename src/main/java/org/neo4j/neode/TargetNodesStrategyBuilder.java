/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;


import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;

import org.neo4j.graphdb.Direction;
import org.neo4j.neode.interfaces.SetRelationshipConstraints;
import org.neo4j.neode.interfaces.SetRelationshipInfo;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public class TargetNodesStrategyBuilder implements SetRelationshipInfo, SetRelationshipConstraints
{
    private final TargetNodesSource targetNodesSource;
    private RelationshipInfo relationshipInfo;

    TargetNodesStrategyBuilder( TargetNodesSource targetNodesSource )
    {
        this.targetNodesSource = targetNodesSource;
    }

    @Override
    public final SetRelationshipConstraints relationship( RelationshipSpecification relationshipSpecification,
                                                          Direction direction )
    {
        relationshipInfo = new RelationshipInfo( relationshipSpecification, direction );
        return this;
    }

    @Override
    public final SetRelationshipConstraints relationship( RelationshipSpecification relationshipSpecification )
    {
        return relationship( relationshipSpecification, Direction.OUTGOING );
    }

    @Override
    public final TargetNodesStrategy relationshipConstraints( Range cardinality,
                                                              ProbabilityDistribution probabilityDistribution,
                                                              RelationshipUniqueness relationshipUniqueness )
    {
        RelationshipConstraints relationshipConstraints = new RelationshipConstraints( cardinality,
                relationshipUniqueness,
                probabilityDistribution );
        return new TargetNodesStrategy( targetNodesSource, relationshipInfo, relationshipConstraints );
    }

    @Override
    public final TargetNodesStrategy relationshipConstraints( Range cardinality,
                                                              RelationshipUniqueness relationshipUniqueness )
    {
        return relationshipConstraints( cardinality, flatDistribution(), relationshipUniqueness );
    }

    @Override
    public final TargetNodesStrategy relationshipConstraints( Range cardinality,
                                                              ProbabilityDistribution probabilityDistribution )
    {
        return relationshipConstraints( cardinality, probabilityDistribution, RelationshipUniqueness.ALLOW_MULTIPLE );
    }

    @Override
    public final TargetNodesStrategy relationshipConstraints( Range cardinality )
    {
        return relationshipConstraints( cardinality, flatDistribution(), RelationshipUniqueness.ALLOW_MULTIPLE );
    }
}
