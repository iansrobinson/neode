package org.neo4j.datasetbuilder;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

import org.neo4j.datasetbuilder.properties.Property;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class DomainEntityBuilder
{
    public static DomainEntityBuilder domainEntity( String name )
    {
        return new DomainEntityBuilder( name );
    }

    private String entityName;
    private List<Property> properties = Collections.emptyList();

    private DomainEntityBuilder( String entityName )
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

    public class LabelledDomainEntity implements DomainEntity
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
