/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder;

import java.util.List;

public class DomainEntityInfo
{
    private final String name;
    private final List<Long> nodeIds;

    public DomainEntityInfo( String name, List<Long> nodeIds )
    {
        this.name = name;
        this.nodeIds = nodeIds;
    }

    public String entityName()
    {
        return name;
    }

    public List<Long> nodeIds()
    {
        return nodeIds;
    }
}
