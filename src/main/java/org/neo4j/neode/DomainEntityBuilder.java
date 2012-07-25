package org.neo4j.neode;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

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

    public DomainEntity build()
    {
        return new LabelledDomainEntity( entityName, properties );
    }

    private class LabelledDomainEntity extends DomainEntity
    {
        private final String entityName;
        private final List<Property> properties;

        private LabelledDomainEntity( String entityName, List<Property> properties )
        {
            this.entityName = entityName;
            this.properties = properties;
        }

        @Override
        public Long build( GraphDatabaseService db, int index )
        {
            Node node = db.createNode();
            node.setProperty( "_label", entityName );
            for ( Property property : properties )
            {
                property.setProperty( db, node, entityName, index );
            }
            return node.getId();
        }

        @Override
        public String entityName()
        {
            return entityName;
        }
    }

}
