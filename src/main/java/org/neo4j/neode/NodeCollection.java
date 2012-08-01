/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NodeCollection
{
    public static NodeCollection approxPercent(int percent, NodeCollection domainEntities)
    {
        if (percent < 1 || percent > 100)
        {
            throw new IllegalArgumentException( "Percent must be between 1 and 100" );
        }

        Random random = new Random();
        List<Long> newNodeIds = new ArrayList<Long>(  );
        for ( Long nodeId : domainEntities.nodeIds() )
        {
            int score = random.nextInt( 100 ) + 1;
            if (score <= percent)
            {
                newNodeIds.add( nodeId );
            }
        }
        return new NodeCollection( domainEntities.entityName(), newNodeIds );
    }

    private final String name;
    private final List<Long> nodeIds;

    public NodeCollection( String name, List<Long> nodeIds )
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

    @Override
    public String toString()
    {
        return "DomainEntityInfo{" +
                "name='" + name + '\'' +
                ", nodeIds=" + nodeIds +
                '}';
    }
}
