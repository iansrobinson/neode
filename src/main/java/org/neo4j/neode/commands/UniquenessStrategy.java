/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

public interface UniquenessStrategy
{
    void apply( GraphDatabaseService db, Node firstNode, Node secondNode,
                RelationshipType relationshipType, Direction direction );
    String description();
}
