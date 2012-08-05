package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.neo4j.kernel.Traversal.expanderForTypes;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.properties.Property;

class RelationshipInfo
{
    private final RelationshipType relationshipType;
    private final Direction direction;
    private final List<Property> properties;

    RelationshipInfo( RelationshipType relationshipType, Direction direction, Property... properties )
    {
        this.relationshipType = relationshipType;
        this.direction = direction;
        this.properties = asList( properties );
    }

    public String description()
    {
        String relStart = "-";
        String relEnd = "->";
        if ( direction.equals( Direction.INCOMING ) )
        {
            relStart = "<-";
            relEnd = "-";
        }
        return String.format( "%s[:%s]%s", relStart, relationshipType.name(), relEnd );
    }

    public Relationship createRelationship( Node firstNode, Node secondNode, GraphDatabaseService db, Random random )
    {
        Relationship rel;
        if ( direction.equals( Direction.OUTGOING ) )
        {
            rel = firstNode.createRelationshipTo( secondNode, relationshipType );
        }
        else
        {
            rel = secondNode.createRelationshipTo( firstNode, relationshipType );
        }
        for ( Property property : properties )
        {
            property.setProperty( rel, db, relationshipType.name(), 0, random );
        }
        return rel;
    }

    public Expander expander()
    {
        return expanderForTypes( relationshipType, direction );
    }

    public Expander expanderIgnoreDirection()
    {
        return expanderForTypes( relationshipType, Direction.BOTH );
    }


}
