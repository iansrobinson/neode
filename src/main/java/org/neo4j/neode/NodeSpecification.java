package org.neo4j.neode;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.interfaces.UpdateDataset;
import org.neo4j.neode.properties.Property;

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

    public UpdateDataset<NodeIdCollection> create( int quantity )
    {
        return new CreateNodesBatchCommandBuilder( this, quantity );
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

    NodeIdCollection emptyNodeIdCollection(int capacity)
    {
        return new NodeIdCollection( label, capacity );
    }

    String label()
    {
        return label;
    }
}
