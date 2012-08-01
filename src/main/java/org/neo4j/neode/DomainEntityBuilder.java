package org.neo4j.neode;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.properties.Property;

public class DomainEntityBuilder
{
    private String entityName;
    private List<Property> properties = Collections.emptyList();

    DomainEntityBuilder( String entityName )
    {
        this.entityName = entityName;
    }

    public DomainEntityBuilder withProperties( Property... values )
    {
        properties = asList( values );
        return this;
    }

    public NodeSpecification build()
    {
        return new LabelledNodeSpecification( entityName, properties );
    }

    private class LabelledNodeSpecification extends NodeSpecification
    {
        private final String entityName;
        private final List<Property> properties;

        private LabelledNodeSpecification( String entityName, List<Property> properties )
        {
            this.entityName = entityName;
            this.properties = properties;
        }

        @Override
        public Node build( GraphDatabaseService db, int index, Random random )
        {
            Node node = db.createNode();
            node.setProperty( "_label", entityName );
            for ( Property property : properties )
            {
                property.setProperty( node, db, entityName, index, random );
            }
            return node;
        }

        @Override
        public String entityName()
        {
            return entityName;
        }
    }

}
