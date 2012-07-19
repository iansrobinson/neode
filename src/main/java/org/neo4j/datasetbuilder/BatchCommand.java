/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder;

import org.neo4j.graphdb.GraphDatabaseService;

public interface BatchCommand<T> extends Results<T>
{
    int numberOfIterations();
    int batchSize();
    void execute( GraphDatabaseService db, int index );
    String description();
    void onBegin(Log log);
    void onEnd(Log log);
}
