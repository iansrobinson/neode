package org.neo4j.neode.properties;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;

class IndexableProperty extends Property
{
    private final String propertyName;
    private final PropertyValueGenerator generator;
    private final String indexName;

    IndexableProperty( String propertyName, PropertyValueGenerator generator )
    {
        this(propertyName, generator, null );
    }

    IndexableProperty( String propertyName, PropertyValueGenerator generator, String indexName )
    {
        this.propertyName = propertyName;
        this.generator = generator;
        this.indexName = indexName;
    }

    @Override
    public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String label,
                             int iteration )
    {
        Object value = generator.generateValue( propertyContainer, label, iteration );
        propertyContainer.setProperty( propertyName, value );

        String name = indexName != null ? indexName : label;

        if ( propertyContainer instanceof Node )
        {
            db.index().forNodes( name ).add( (Node) propertyContainer, propertyName, value );
        }
        else
        {
            db.index().forRelationships( name ).add( (Relationship) propertyContainer, propertyName, value );
        }
    }
}
