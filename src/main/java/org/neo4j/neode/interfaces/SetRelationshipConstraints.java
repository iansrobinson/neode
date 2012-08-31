/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.interfaces;

import org.neo4j.neode.TargetNodes;
import org.neo4j.neode.Range;
import org.neo4j.neode.RelationshipUniqueness;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public interface SetRelationshipConstraints
{
    TargetNodes relationshipConstraints( Range cardinality,
                                                      ProbabilityDistribution probabilityDistribution,
                                                      RelationshipUniqueness relationshipUniqueness );

    TargetNodes relationshipConstraints( Range cardinality,
                                                      RelationshipUniqueness relationshipUniqueness );

    TargetNodes relationshipConstraints( Range cardinality,
                                                      ProbabilityDistribution probabilityDistribution );

    TargetNodes relationshipConstraints( Range cardinality );
}
