package org.neo4j.neode.commands;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.properties.Property;

public class NodeSpecification
{
    private final GraphDatabaseService db;
    private final String label;
    private final List<Property> properties;

    public NodeSpecification( GraphDatabaseService db, String label, Property... properties )
    {
        this.db = db;
        this.label = label;
        this.properties = asList( properties );
    }

    public UpdateDataset<NodeCollection> create( int quantity )
    {
        return new NodeBatchCommandBuilder( this, quantity );
    }

    Node build( int iteration, Random random )
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
