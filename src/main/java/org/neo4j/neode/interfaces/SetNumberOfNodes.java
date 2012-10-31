/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.interfaces;

import org.neo4j.neode.Range;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public interface SetNumberOfNodes
{
    SetRelationshipInfo numberOfNodes(int numberOfNodes);
    SetRelationshipInfo numberOfNodes(Range numberOfNodes);
    SetRelationshipInfo numberOfNodes(Range numberOfNodes, ProbabilityDistribution probabilityDistribution);
}
