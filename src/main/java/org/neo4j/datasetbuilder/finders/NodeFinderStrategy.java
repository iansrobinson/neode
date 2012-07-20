/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder.finders;

import java.util.Random;

import org.neo4j.datasetbuilder.commands.DomainEntityBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public interface NodeFinderStrategy
{
    Iterable<Node> getNodes( GraphDatabaseService db, int numberOfNodes, DomainEntityBuilder domainEntityBuilder, Random random );
}
