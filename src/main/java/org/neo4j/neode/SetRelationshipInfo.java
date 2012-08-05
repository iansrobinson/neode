/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.properties.Property;

public interface SetRelationshipInfo
{
    SetRelationshipConstraints relationship( RelationshipType relationshipType, Direction direction,
                                             Property... properties );

    SetRelationshipConstraints relationship( RelationshipType relationshipType, Property... properties );

    SetRelationshipConstraints relationship( String relationshipLabel, Direction direction, Property... properties );

    SetRelationshipConstraints relationship( String relationshipLabel, Property... properties );
}
