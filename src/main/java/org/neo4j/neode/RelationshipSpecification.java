package org.neo4j.neode;

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

public class RelationshipSpecification
{
    private final RelationshipType label;
    private final List<Property> properties;

    RelationshipSpecification( RelationshipType label, List<Property> properties )
    {
        this.label = label;
        this.properties = properties;
    }

    public Relationship createRelationship( Node startNode, Node endNode, GraphDatabaseService db, Random random )
    {
        Relationship rel = startNode.createRelationshipTo( endNode, label );

        for ( Property property : properties )
        {
            property.setProperty( rel, db, label.name(), 0, random );
        }

        return rel;
    }

    public Expander expander( Direction direction )
    {
        return expanderForTypes( label, direction );
    }
}
