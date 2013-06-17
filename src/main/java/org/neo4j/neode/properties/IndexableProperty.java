package org.neo4j.neode.properties;

import java.util.Collections;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;

import static java.util.Arrays.asList;

class IndexableProperty extends Property
{
    private final String propertyName;
    private final PropertyValueGenerator generator;
    private final List<String> indexNames;

    IndexableProperty( String propertyName, PropertyValueGenerator generator )
    {
        this.propertyName = propertyName;
        this.generator = generator;
        this.indexNames = Collections.emptyList();

    }

    IndexableProperty( String propertyName, PropertyValueGenerator generator, String... indexNames )
    {
        this.propertyName = propertyName;
        this.generator = generator;
        this.indexNames = asList( indexNames );

    }

    @Override
    public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String label,
                             int iteration )
    {
        Object value = generator.generateValue( propertyContainer, label, iteration );
        propertyContainer.setProperty( propertyName, value );

        if ( indexNames.isEmpty() )
        {
            indexProperty( propertyContainer, db, label, value );
        }
        else
        {
            for ( String indexName : indexNames )
            {
                indexProperty( propertyContainer, db, indexName, value );
            }
        }
    }

    private void indexProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String indexName,
                                Object value )
    {
        if ( propertyContainer instanceof Node )
        {
            db.index().forNodes( indexName ).add( (Node) propertyContainer, propertyName, value );
        }
        else
        {
            db.index().forRelationships( indexName ).add( (Relationship) propertyContainer, propertyName, value );
        }
    }
}
