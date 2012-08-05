/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.Log;

public interface BatchCommand<T>
{
    int numberOfIterations();
    int batchSize();
    void execute( GraphDatabaseService db, int iteration, Random random );
    void execute( Node firstNode, GraphDatabaseService db, int iteration, Random random );
    String description();
    String shortDescription();
    void onBegin(Log log);
    void onEnd(Log log);
    T results();
}
