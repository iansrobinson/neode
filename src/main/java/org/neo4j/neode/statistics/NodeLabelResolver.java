/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.statistics;

import org.neo4j.graphdb.Node;

public interface NodeLabelResolver
{
    String labelFor( Node node );
}
