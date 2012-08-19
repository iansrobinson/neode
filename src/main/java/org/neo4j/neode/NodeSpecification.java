package org.neo4j.neode;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.interfaces.UpdateDataset;
import org.neo4j.neode.properties.Property;

public class NodeSpecification
{
    private final String label;
    private final List<Property> properties;

    NodeSpecification( String label, List<Property> properties )
    {
        this.label = label;
        this.properties = properties;
    }

    public UpdateDataset<NodeCollection> create( int quantity )
    {
        return new CreateNodesBatchCommandBuilder( this, quantity );
    }

    Node build( GraphDatabaseService db, int iteration, Random random )
    {
        Node node = db.createNode();
        node.setProperty( "_label", label );
        for ( Property property : properties )
        {
            property.setProperty( node, db, label, iteration, random );
        }
        return node;
    }

    String label()
    {
        return label;
    }
}
