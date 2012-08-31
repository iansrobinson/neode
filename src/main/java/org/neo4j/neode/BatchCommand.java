/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import org.neo4j.neode.logging.Log;

interface BatchCommand<T>
{
    int numberOfIterations();
    int batchSize();
    void execute( int iteration );
    String description();
    String shortDescription();
    void onBegin(Log log);
    void onEnd(Log log);
    T results();
}
