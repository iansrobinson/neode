package org.neo4j.neode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.interfaces.UpdateDataset;
import org.neo4j.neode.properties.Property;
import org.neo4j.tooling.GlobalGraphOperations;

public class NodeSpecification
{
    private final Label label;
    private final List<Property> properties;
    private final GraphDatabaseService db;

    NodeSpecification( String label, List<Property> properties, GraphDatabaseService db )
    {
        this( DynamicLabel.label(label), properties, db );
    }

    NodeSpecification( Label label, List<Property> properties, GraphDatabaseService db )
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
        Iterable<Node> allNodes = GlobalGraphOperations.at( db ).getAllNodesWithLabel( label );
        Set<Long> nodeIds = new HashSet<>();
        for ( Node node : allNodes )
        {
            nodeIds.add( node.getId() );
        }
        return newNodeCollection( nodeIds );
    }


    Node build( int iteration )
    {
        Node node = db.createNode( label );
        for ( Property property : properties )
        {
            property.setProperty( node, label.name(), iteration );
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

    String labelName()
    {
        return label.name();
    }

    Label label()
    {
        return label;
    }
}
