package org.neo4j.neode.commands;

import static org.neo4j.kernel.Traversal.expanderForTypes;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

class RelationshipInfo
{
    private final RelationshipType relationshipType;
    private final Direction direction;

    public RelationshipInfo( RelationshipType relationshipType, Direction direction )
    {
        this.relationshipType = relationshipType;
        this.direction = direction;
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

    public Relationship createRelationship( Node firstNode, Node secondNode )
    {
        if ( direction.equals( Direction.OUTGOING ) )
        {
            return firstNode.createRelationshipTo( secondNode, relationshipType );
        }
        else
        {
            return secondNode.createRelationshipTo( firstNode, relationshipType );
        }
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
