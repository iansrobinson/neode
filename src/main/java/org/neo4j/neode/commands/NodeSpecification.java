package org.neo4j.neode.commands;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.interfaces.UpdateDataset;
import org.neo4j.neode.properties.Property;

public class NodeSpecification
{
    private final String label;
    private final List<Property> properties;

    public NodeSpecification( String label, Property... properties )
    {
        this.label = label;
        this.properties = asList( properties );
    }

    Node build( GraphDatabaseService db, int index, Random random )
    {
        Node node = db.createNode();
        node.setProperty( "_label", label );
        for ( Property property : properties )
        {
            property.setProperty( node, db, label, index, random );
        }
        return node;
    }

    public String label()
    {
        return label;
    }

    public UpdateDataset<NodeCollection> create( int quantity )
    {
        return new NodeBatchCommandBuilder( this, quantity );
    }
}
