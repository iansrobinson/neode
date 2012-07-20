/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.graphdb.RelationshipType;

public interface RelationshipName
{
    NumberOfRels relationship( RelationshipType value );
}
