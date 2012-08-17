package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

public class IndexableProperty extends Property
{
    private final String propertyName;
    private final PropertyValueGenerator generator;
    private final String indexName;
    private Index<Node> nodeIndex;

    public IndexableProperty( String propertyName, PropertyValueGenerator generator, String indexName )
    {
        this.propertyName = propertyName;
        this.generator = generator;
        this.indexName = indexName;
    }

    @Override
    public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String nodeLabel,
                             int iteration, Random random )
    {
        Object value = generator.generateValue( propertyContainer, nodeLabel, iteration, random );
        propertyContainer.setProperty( propertyName, value );

        if ( propertyContainer instanceof Node )
        {
            if (nodeIndex == null)
            {
                nodeIndex = db.index().forNodes( indexName != null ? indexName : nodeLabel );
            }
            nodeIndex.add( (Node) propertyContainer, propertyName, value );
        }
    }
}
