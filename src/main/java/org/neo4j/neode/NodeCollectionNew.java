package org.neo4j.neode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.interfaces.UpdateDataset;

public class NodeCollectionNew implements Iterable<Node>
{
    public static final NodeCollectionNew NULL = new NodeCollectionNew( null, null, new NullList<Long>() );

    private final GraphDatabaseService db;
    private final String label;
    private final List<Long> nodeIds;

    NodeCollectionNew( GraphDatabaseService db, String label, int capacity )
    {
        this( db, label, new ArrayList<Long>( capacity ) );
    }

    NodeCollectionNew( GraphDatabaseService db, String label, List<Long> nodeIds )
    {
        this.db = db;
        this.label = label;
        this.nodeIds = nodeIds;
    }

    public void add( Node node )
    {
        nodeIds.add( node.getId() );
    }

    public Node getNode( int position )
    {
        return db.getNodeById( nodeIds.get( position ) );
    }

    public Long getNodeId( int position )
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
    public Iterator<Node> iterator()
    {
        return new Iterator<Node>()
        {
            int position = 0;

            @Override
            public boolean hasNext()
            {
                return position < nodeIds.size();
            }

            @Override
            public Node next()
            {
                Long id = nodeIds.get( position++ );
                return id != null ? db.getNodeById( id ) : null;
            }

            @Override
            public void remove()
            {
                throw new IllegalStateException();
            }
        };
    }

    public NodeCollectionNew subset( List<Integer> positions )
    {
        List<Long> newNodeIds = new ArrayList<Long>( positions.size() );
        for ( Integer position : positions )
        {
            newNodeIds.add( nodeIds.get( position ) );
        }
        return new NodeCollectionNew( db, label, newNodeIds );
    }

    public NodeCollectionNew approxPercentage( int percentage )
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
        return new NodeCollectionNew( db, label, newNodeIds );
    }

    public NodeCollectionNew combine( NodeCollectionNew other )
    {
        List<Long> newNodeIds = new ArrayList<Long>( nodeIds.size() + other.nodeIds.size() );
        newNodeIds.addAll( nodeIds );
        newNodeIds.addAll( other.nodeIds );
        return new NodeCollectionNew( db, label, newNodeIds );
    }

    public UpdateDataset<NodeCollection> createRelationshipsTo( CreateRelationshipSpecification createRelationshipSpecification )
    {
        return new RelateNodesBatchCommandBuilder( new NodeCollection( label, nodeIds ), createRelationshipSpecification );
    }

    public UpdateDataset<List<NodeCollection>> createRelationshipsTo(
            CreateRelationshipSpecificationChoices createRelationshipSpecificationChoices )
    {
        return new RelateToChoiceOfNodesBatchCommandBuilder( toNodeCollection(),
                createRelationshipSpecificationChoices );
    }

    public NodeCollection toNodeCollection()
    {
        return new NodeCollection( label, nodeIds );
    }


}
