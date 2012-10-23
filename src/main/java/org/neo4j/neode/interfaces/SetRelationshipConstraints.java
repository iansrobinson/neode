/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.interfaces;

import org.neo4j.neode.Range;
import org.neo4j.neode.RelationshipUniqueness;
import org.neo4j.neode.TargetNodesStrategy;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public interface SetRelationshipConstraints
{
    TargetNodesStrategy relationshipConstraints( Range cardinality,
                                                 ProbabilityDistribution probabilityDistribution,
                                                 RelationshipUniqueness relationshipUniqueness );

    TargetNodesStrategy relationshipConstraints( Range cardinality,
                                                 RelationshipUniqueness relationshipUniqueness );

    TargetNodesStrategy relationshipConstraints( Range cardinality,
                                                 ProbabilityDistribution probabilityDistribution );

    TargetNodesStrategy relationshipConstraints( Range cardinality );

    TargetNodesStrategy relationshipConstraints( ProbabilityDistribution probabilityDistribution,
                                                 RelationshipUniqueness relationshipUniqueness );

    TargetNodesStrategy relationshipConstraints( RelationshipUniqueness relationshipUniqueness );

    TargetNodesStrategy relationshipConstraints( ProbabilityDistribution probabilityDistribution );

    TargetNodesStrategy exactlyOneRelationship();

}
