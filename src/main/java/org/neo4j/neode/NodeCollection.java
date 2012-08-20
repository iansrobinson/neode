/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.neode.interfaces.UpdateDataset;

public class NodeCollection
{
    public static NodeCollection approxPercent( int percent, NodeCollection nodeCollection )
    {
        if ( percent < 1 || percent > 100 )
        {
            throw new IllegalArgumentException( "Percent must be between 1 and 100" );
        }

        Random random = new Random();

        int arraySize = nodeCollection.size() * ((percent + 10) / 100);
        List<Long> newNodeIds = new ArrayList<Long>( arraySize );
        for ( Long nodeId : nodeCollection.nodeIds )
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

    public int size()
    {
        return nodeIds.size();
    }

    public NodeCollection combine( NodeCollection otherCollection )
    {
        if (!otherCollection.label().equals( name ))
        {
            throw new IllegalArgumentException(
                    String.format("Invalid label. Expected: %s. Received: %s.", name, otherCollection.label() ));
        }

        List<Long> newNodeIds = new ArrayList<Long>( nodeIds.size() + otherCollection.size() );
        newNodeIds.addAll( nodeIds );
        newNodeIds.addAll( otherCollection.nodeIds );
        return new NodeCollection( name, newNodeIds );
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

    Long getId(int index)
    {
        return nodeIds.get( index );
    }
}
