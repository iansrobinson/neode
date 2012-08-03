package org.neo4j.neode.commands;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.properties.Property;

public class NodeSpecification
{
    private static final int DEFAULT_BATCH_SIZE = 20000;

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

    public NodeCollection create( int quantity, Dataset dataset )
    {
        return create( quantity, dataset, DEFAULT_BATCH_SIZE );
    }

    public NodeCollection create( int quantity, Dataset dataset, int batchSize )
    {
        NodeBatchCommand command = new NodeBatchCommand( this, quantity, batchSize, new UniqueNodeIdCollector() );
        return dataset.execute( command );
    }
}
