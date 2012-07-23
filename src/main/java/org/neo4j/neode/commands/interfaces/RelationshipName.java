/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands.interfaces;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.RelationshipType;

public interface RelationshipName
{
    Cardinality relationship( RelationshipType value );
    Cardinality relationship( RelationshipType value, Direction direction);
}
