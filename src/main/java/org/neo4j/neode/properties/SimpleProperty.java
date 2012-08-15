package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

class SimpleProperty extends Property
{
    private final String propertyName;
    private final PropertyValueGenerator generator;
    private final boolean isIndexable;
    private final String indexName;
    private Index<Node> nodeIndex;

    SimpleProperty( String propertyName, PropertyValueGenerator generator, boolean isIndexable, String indexName )
    {
        this.propertyName = propertyName;
        this.generator = generator;
        this.isIndexable = isIndexable;
        this.indexName = indexName;
    }

    SimpleProperty( String propertyName, PropertyValueGenerator generator, boolean isIndexable )
    {
        this( propertyName, generator, isIndexable, null );
    }

    @Override
    public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String nodeLabel,
                             int iteration, Random random )
    {
        Object value = generator.generateValue( propertyContainer, nodeLabel, iteration, random );
        propertyContainer.setProperty( propertyName, value );

        if ( isIndexable && propertyContainer instanceof Node )
        {
            if ( nodeIndex == null )
            {
                nodeIndex = db.index().forNodes( indexName == null ? nodeLabel : indexName );
            }
            nodeIndex.add( (Node) propertyContainer, propertyName, value );
        }
    }
}
