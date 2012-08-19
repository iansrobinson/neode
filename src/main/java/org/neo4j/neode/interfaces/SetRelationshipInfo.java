/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.interfaces;

import org.neo4j.graphdb.Direction;
import org.neo4j.neode.RelationshipSpecification;

public interface SetRelationshipInfo
{
    SetRelationshipConstraints relationship( RelationshipSpecification relationshipSpecification, Direction direction );

    SetRelationshipConstraints relationship( RelationshipSpecification relationshipSpecification );
}
