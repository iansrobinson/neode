/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.interfaces;

import org.neo4j.neode.Range;
import org.neo4j.neode.RelationshipUniqueness;
import org.neo4j.neode.TargetNodesSpecification;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public interface SetRelationshipConstraints
{
    TargetNodesSpecification relationshipConstraints( Range cardinality,
                                                      ProbabilityDistribution probabilityDistribution,
                                                      RelationshipUniqueness relationshipUniqueness );

    TargetNodesSpecification relationshipConstraints( Range cardinality,
                                                      RelationshipUniqueness relationshipUniqueness );

    TargetNodesSpecification relationshipConstraints( Range cardinality,
                                                      ProbabilityDistribution probabilityDistribution );

    TargetNodesSpecification relationshipConstraints( Range cardinality );
}
