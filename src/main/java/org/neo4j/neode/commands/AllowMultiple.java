package org.neo4j.neode.commands;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

public class AllowMultiple implements UniquenessStrategy
{
    public static UniquenessStrategy allowMultiple()
    {
        return new AllowMultiple();
    }

    private AllowMultiple()
    {
    }

    @Override
    public void apply( GraphDatabaseService db, Node firstNode, Node secondNode, RelationshipType relationshipType,
                       Direction direction )
    {
        if ( direction.equals( Direction.OUTGOING ) )
        {
            firstNode.createRelationshipTo( secondNode, relationshipType );
        }
        else
        {
            secondNode.createRelationshipTo( firstNode, relationshipType );
        }
    }

    @Override
    public String description()
    {
        return "Allow multiple";
    }
}
