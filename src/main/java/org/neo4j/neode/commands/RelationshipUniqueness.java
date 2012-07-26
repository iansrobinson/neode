/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

interface RelationshipUniqueness
{
    Relationship createRelationship( GraphDatabaseService db, Node firstNode, Node secondNode,
                                     RelationshipInfo relationshipInfo );
}
