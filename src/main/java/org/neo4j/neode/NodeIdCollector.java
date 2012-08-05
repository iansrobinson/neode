/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import java.util.List;

interface NodeIdCollector
{
    void add(Long nodeId);
    List<Long> nodeIds();
}
