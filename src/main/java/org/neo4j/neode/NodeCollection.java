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
    public static NodeCollection approxPercent( int percent, NodeCollection nodeCollection )
    {
        if ( percent < 1 || percent > 100 )
        {
            throw new IllegalArgumentException( "Percent must be between 1 and 100" );
        }

        Random random = new Random();
        List<Long> newNodeIds = new ArrayList<Long>();
        for ( Long nodeId : nodeCollection.nodeIds() )
        {
            int score = random.nextInt( 100 ) + 1;
            if ( score <= percent )
            {
                newNodeIds.add( nodeId );
            }
        }
        return new NodeCollection( nodeCollection.label(), newNodeIds );
    }

    private final String name;
    private final List<Long> nodeIds;

    NodeCollection( String name, List<Long> nodeIds )
    {
        this.name = name;
        this.nodeIds = nodeIds;
    }

    public String label()
    {
        return name;
    }

    public List<Long> nodeIds()
    {
        return nodeIds;
    }

    public UpdateDataset<NodeCollection> createRelationshipsTo( TargetNodesSpecification targetNodesSpecification )
    {
        return new RelateNodesBatchCommandBuilder( this, targetNodesSpecification );
    }

    public UpdateDataset<List<NodeCollection>> createRelationshipsTo( TargetNodesSpecificationsChoices targetNodesSpecificationsChoices )
    {
        return new RelateToChoiceOfNodesBatchCommandBuilder( this, targetNodesSpecificationsChoices );
    }

    @Override
    public String toString()
    {
        return "NodeCollection{" +
                "name='" + name + '\'' +
                ", nodeIds=" + nodeIds +
                '}';
    }
}
