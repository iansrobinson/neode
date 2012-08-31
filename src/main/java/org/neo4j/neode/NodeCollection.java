package org.neo4j.neode;

import java.util.Iterator;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.interfaces.UpdateDataset;

public class NodeCollection implements Iterable<Node>
{
    public static final NodeCollection NULL = new NodeCollection( null, NodeIdCollection.NULL );

    private final GraphDatabaseService db;
    private final NodeIdCollection nodeIdCollection;

    NodeCollection( GraphDatabaseService db, NodeIdCollection nodeIdCollection )
    {
        this.db = db;
        this.nodeIdCollection = nodeIdCollection;
    }

    public void add( Node node )
    {
        nodeIdCollection.add( node.getId() );
    }

    public Node getNodeByPosition( int position )
    {
        return db.getNodeById( nodeIdCollection.getIdByPosition( position ) );
    }

    public String label()
    {
        return nodeIdCollection.label();
    }

    public int size()
    {
        return nodeIdCollection.size();
    }

    @Override
    public Iterator<Node> iterator()
    {
        final Iterator<Long> idIterator = nodeIdCollection.iterator();

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

    public NodeCollection subset( List<Integer> positions )
    {
        return new NodeCollection( db, nodeIdCollection.subset( positions ) );
    }

    public NodeCollection approxPercentage( int percentage )
    {
        return new NodeCollection( db, nodeIdCollection.approxPercentage( percentage ) );
    }

    public NodeCollection combine( NodeCollection other )
    {
        return new NodeCollection( db, nodeIdCollection.combine( other.nodeIdCollection ) );
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
}
