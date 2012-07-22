package org.neo4j.datasetbuilder;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class DomainEntity
{
    public static DomainEntity domainEntity( String name )
    {
        return new DomainEntity( name, Property.property( "name" ) );
    }

    public static DomainEntity domainEntity( String name, Property property )
    {
        return new DomainEntity( name, property );
    }

    private final String entityName;
    private final Property property;

    private DomainEntity( String entityName, Property property )
    {
        this.entityName = entityName;
        this.property = property;
    }

    public Long build( GraphDatabaseService db, int index )
    {
        Node node = db.createNode();
        node.setProperty( "_label", entityName );
        property.setProperty( db, node, entityName, index );
        return node.getId();
    }

    public String entityName()
    {
        return entityName;
    }
}
