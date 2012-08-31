package org.neo4j.neode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.neode.interfaces.UpdateDataset;

public class NodeIdCollection implements Iterable<Long>
{
    public static final NodeIdCollection NULL = new NodeIdCollection( null, new NullList<Long>() );

    private final String label;
    private final List<Long> nodeIds;

    NodeIdCollection( String label, int capacity )
    {
        this( label, new ArrayList<Long>( capacity ) );
    }

    NodeIdCollection( String label, List<Long> nodeIds )
    {
        this.label = label;
        this.nodeIds = nodeIds;
    }

    public void add( Long id )
    {
        nodeIds.add( id );
    }

    public Long getIdByPosition( int position )
    {
        return nodeIds.get( position );
    }

    public String label()
    {
        return label;
    }

    public int size()
    {
        return nodeIds.size();
    }

    @Override
    public Iterator<Long> iterator()
    {
        return nodeIds.iterator();
    }

    public NodeIdCollection subset( List<Integer> positions )
    {
        List<Long> newNodeIds = new ArrayList<Long>( positions.size() );
        for ( Integer position : positions )
        {
            newNodeIds.add( nodeIds.get( position ) );
        }
        return new NodeIdCollection( label, newNodeIds );
    }

    public NodeIdCollection approxPercentage( int percentage )
    {
        if ( percentage < 1 || percentage > 100 )
        {
            throw new IllegalArgumentException( "Percent must be between 1 and 100" );
        }

        Random random = new Random();

        int arraySize = nodeIds.size() * ((percentage + 10) / 100);
        List<Long> newNodeIds = new ArrayList<Long>( arraySize );
        for ( Long nodeId : nodeIds )
        {
            int score = random.nextInt( 100 ) + 1;
            if ( score <= percentage )
            {
                newNodeIds.add( nodeId );
            }
        }
        return new NodeIdCollection( label, newNodeIds );
    }

    public NodeIdCollection combine( NodeIdCollection other )
    {
        List<Long> newNodeIds = new ArrayList<Long>( nodeIds.size() + other.nodeIds.size() );
        newNodeIds.addAll( nodeIds );
        newNodeIds.addAll( other.nodeIds );
        return new NodeIdCollection( label, newNodeIds );
    }

    public UpdateDataset<NodeIdCollection> createRelationshipsTo( TargetNodes
                                                                          targetNodes )
    {
        return new RelateNodesBatchCommandBuilder( this, targetNodes );
    }

    public UpdateDataset<List<NodeIdCollection>> createRelationshipsTo(
            CreateRelationshipSpecificationChoices createRelationshipSpecificationChoices )
    {
        return new RelateToChoiceOfNodesBatchCommandBuilder( this, createRelationshipSpecificationChoices );
    }
}
