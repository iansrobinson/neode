package org.neo4j.neode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.interfaces.UpdateDataset;
import org.neo4j.neode.properties.Property;
import org.neo4j.tooling.GlobalGraphOperations;

public class NodeSpecification
{
    private final String label;
    private final List<Property> properties;
    private final GraphDatabaseService db;

    NodeSpecification( String label, List<Property> properties, GraphDatabaseService db )
    {
        this.label = label;
        this.properties = properties;
        this.db = db;
    }

    public UpdateDataset<NodeCollection> create( int quantity )
    {
        return new CreateNodesBatchCommandBuilder( this, quantity );
    }

    public NodeCollection getAll()
    {
        Iterable<Node> allNodes = GlobalGraphOperations.at( db ).getAllNodes();
        Set<Long> nodeIds = new HashSet<>();
        for ( Node node : allNodes )
        {
            if ( node.hasProperty( "_label" ) && node.getProperty( "_label" ).equals( label ) )
            {
                nodeIds.add( node.getId() );
            }
        }
        return newNodeCollection( nodeIds );
    }


    Node build( int iteration )
    {
        Node node = db.createNode();
        node.setProperty( "_label", label );
        for ( Property property : properties )
        {
            property.setProperty( node, db, label, iteration );
        }
        return node;
    }

    NodeCollection emptyNodeCollection( int capacity )
    {
        return new NodeCollection( db, label, new HashSet<Long>( capacity ) );
    }

    NodeCollection newNodeCollection( Set<Long> nodeIds )
    {
        return new NodeCollection( db, label, nodeIds );
    }

    String label()
    {
        return label;
    }
}
