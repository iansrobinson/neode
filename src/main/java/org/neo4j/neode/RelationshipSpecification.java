package org.neo4j.neode;

import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Expander;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.properties.Property;

import static org.neo4j.kernel.Traversal.expanderForTypes;

public class RelationshipSpecification
{
    private final RelationshipType relationshipType;
    private final List<Property> properties;
    private final GraphDatabaseService db;

    RelationshipSpecification( RelationshipType relationshipType, List<Property> properties, GraphDatabaseService db )
    {
        this.relationshipType = relationshipType;
        this.properties = properties;
        this.db = db;
    }

    Relationship createRelationship( Node startNode, Node endNode, int iteration )
    {
        Relationship rel = startNode.createRelationshipTo( endNode, relationshipType );

        for ( Property property : properties )
        {
            property.setProperty( rel, relationshipType.name(), iteration );
        }

        return rel;
    }

    String label()
    {
        return relationshipType.name();
    }

    Expander expander( Direction direction )
    {
        return expanderForTypes( relationshipType, direction );
    }
}
