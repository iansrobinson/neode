package org.neo4j.neode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.interfaces.UpdateDataset;

public class NodeCollection implements Iterable<Node>
{
    public static final NodeCollection NULL = new NodeCollection( null, null, new NullSet<Long>() );

    private final GraphDatabaseService db;
    private final String label;
    private final Set<Long> nodeIds;

    NodeCollection( GraphDatabaseService db, String label, Set<Long> nodeIds )
    {
        this.db = db;
        this.label = label;
        this.nodeIds = nodeIds;
    }

    void add( Node node )
    {
        nodeIds.add( node.getId() );
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
        final Iterator<Long> idIterator = nodeIds.iterator();

        return new Iterator<Node>()
        {
            @Override
            public boolean hasNext()
            {
                return idIterator.hasNext();
            }

            @Override
            public Node next()
            {
                Long id = idIterator.next();
                return id != null ? db.getNodeById( id ) : null;
            }

            @Override
            public void remove()
            {
                idIterator.remove();
            }
        };
    }

    NodeCollection subset( List<Integer> positions )
    {
        ArrayList<Long> nodeIdList = new ArrayList<Long>( nodeIds );

        Set<Long> newNodeIds = new HashSet<Long>( positions.size() );
        for ( Integer position : positions )
        {
            newNodeIds.add( nodeIdList.get( position ) );
        }

        return new NodeCollection( db, label, newNodeIds );
    }

    public NodeCollection approxPercentage( int percentage )
    {
        if ( percentage < 1 || percentage > 100 )
        {
            throw new IllegalArgumentException( "Percent must be between 1 and 100" );
        }

        Random random = new Random();

        int arraySize = nodeIds.size() * ((percentage + 10) / 100);
        Set<Long> newNodeIds = new HashSet<Long>( arraySize );
        for ( Long nodeId : nodeIds )
        {
            int score = random.nextInt( 100 ) + 1;
            if ( score <= percentage )
            {
                newNodeIds.add( nodeId );
            }
        }
        return new NodeCollection( db, label, newNodeIds );
    }

    public NodeCollection combine( NodeCollection other )
    {
        Set<Long> newNodeIds = new HashSet<Long>( nodeIds.size() + other.nodeIds.size() );
        newNodeIds.addAll( nodeIds );
        newNodeIds.addAll( other.nodeIds );
        return new NodeCollection( db, label, newNodeIds );
    }

    public UpdateDataset<NodeCollection> createRelationshipsTo( TargetNodesStrategy targetNodesStrategy )
    {
        return new RelateNodesBatchCommandBuilder( this, targetNodesStrategy );
    }

    public UpdateDataset<List<NodeCollection>> createRelationshipsTo(
            ChoiceOfTargetNodesStrategy choiceOfTargetNodesStrategy )
    {
        return new RelateToChoiceOfNodesBatchCommandBuilder( this, choiceOfTargetNodesStrategy );
    }

    NodeList toNodeList()
    {
        return new NodeList( db, label, nodeIds );
    }

    static class NodeList
    {
        private final GraphDatabaseService db;
        private final String label;
        private final List<Long> nodeIds;

        NodeList( GraphDatabaseService db, String label, Collection<Long> nodeIds )
        {
            this.db = db;
            this.label = label;
            this.nodeIds = new ArrayList<Long>( nodeIds );
        }

        Node getNodeByPosition( int position )
        {
            return db.getNodeById( nodeIds.get( position ) );
        }

        String label()
        {
            return label;
        }

        int size()
        {
            return nodeIds.size();
        }
    }
}
