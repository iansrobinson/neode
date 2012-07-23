/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder;

import org.neo4j.graphdb.GraphDatabaseService;

public interface DomainEntity
{
    Long build( GraphDatabaseService db, int index );

    String entityName();
}
