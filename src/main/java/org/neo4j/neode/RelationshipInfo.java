package org.neo4j.neode;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

class RelationshipInfo
{
    private final RelationshipSpecification relationshipSpecification;
    private final Direction direction;

    RelationshipInfo( RelationshipSpecification relationshipSpecification,
                      Direction direction )
    {
        if ( direction.equals( Direction.BOTH ) )
        {
            throw new IllegalArgumentException( "Direction must be either INCOMING or OUTGOING." );
        }

        this.relationshipSpecification = relationshipSpecification;
        this.direction = direction;
    }

    public String description()
    {
        String prefix = "-";
        String suffix = "->";
        if ( direction.equals( Direction.INCOMING ) )
        {
            prefix = "<-";
            suffix = "-";
        }
        return String.format( "%s[:%s]%s", prefix, relationshipSpecification.label(), suffix );
    }

    public Relationship createRelationship( Node firstNode, Node secondNode, int iteration )
    {
        if ( direction.equals( Direction.OUTGOING ) )
        {
            return relationshipSpecification.createRelationship( firstNode, secondNode, iteration );
        }
        else
        {
            return relationshipSpecification.createRelationship( secondNode, firstNode, iteration );
        }
    }

    public Expander expander()
    {
        return relationshipSpecification.expander( direction );
    }

    public Expander expanderIgnoreDirection()
    {
        return relationshipSpecification.expander( Direction.BOTH );
    }
}
