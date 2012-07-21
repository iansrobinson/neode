package org.neo4j.datasetbuilder;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class DomainEntity
{
    public static DomainEntity domainEntity( String name )
    {
        return new DomainEntity( name, "name", false );
    }

    public static DomainEntity domainEntity( String name, String propertyName )
    {
        return new DomainEntity( name, propertyName, false );
    }

    public static DomainEntity domainEntity( String name, boolean isIndexable )
    {
        return new DomainEntity( name, "name", isIndexable );
    }

    public static DomainEntity domainEntity( String name, String propertyName, boolean isIndexable )
    {
        return new DomainEntity( name, propertyName, isIndexable );
    }


    private final String entityName;
    private final String propertyName;
    private boolean isIndexable;

    private DomainEntity( String entityName, String propertyName, boolean isIndexable )
    {
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.isIndexable = isIndexable;
    }

    public Long build( GraphDatabaseService db, int index )
    {
        Node node = db.createNode();
        String value = String.format( "%s-%s", entityName, index + 1 );
        node.setProperty( "_label", entityName );
        node.setProperty( propertyName, value );
        if ( isIndexable )
        {
            db.index().forNodes( entityName ).add( node, propertyName, value );
        }
        return node.getId();
    }

    public String entityName()
    {
        return entityName;
    }
}
