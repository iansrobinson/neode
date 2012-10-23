/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.interfaces;

import org.neo4j.neode.Range;

public interface SetNumberOfNodes
{
    SetRelationshipInfo numberOfNodes(int numberOfNodes);
    SetRelationshipInfo numberOfNodes(Range numberOfNodes);
}
