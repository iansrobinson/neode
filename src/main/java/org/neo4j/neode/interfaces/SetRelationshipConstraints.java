/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.interfaces;

import org.neo4j.neode.CreateRelationshipSpecification;
import org.neo4j.neode.Range;
import org.neo4j.neode.RelationshipUniqueness;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public interface SetRelationshipConstraints
{
    CreateRelationshipSpecification relationshipConstraints( Range cardinality,
                                                      ProbabilityDistribution probabilityDistribution,
                                                      RelationshipUniqueness relationshipUniqueness );

    CreateRelationshipSpecification relationshipConstraints( Range cardinality,
                                                      RelationshipUniqueness relationshipUniqueness );

    CreateRelationshipSpecification relationshipConstraints( Range cardinality,
                                                      ProbabilityDistribution probabilityDistribution );

    CreateRelationshipSpecification relationshipConstraints( Range cardinality );
}
