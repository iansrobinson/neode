package org.neo4j.neode.commands;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.commands.interfaces.SetRelationshipConstraints;
import org.neo4j.neode.commands.interfaces.SetRelationshipInfo;
import org.neo4j.neode.numbergenerators.Range;
import org.neo4j.neode.properties.Property;

public class RelationshipDescriptionBuilder implements SetRelationshipInfo, SetRelationshipConstraints
{
    private final NodeFinder nodes;
    private RelationshipInfo relationshipInfo;
    private RelationshipConstraints relationshipConstraints;

    public RelationshipDescriptionBuilder( NodeFinder nodes )
    {
        this.nodes = nodes;
    }

    @Override
    public RelationshipDescriptionBuilder relationship( RelationshipType relationshipType, Direction direction,
                                                        Property... properties )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, direction, properties );
        return this;
    }

    @Override
    public RelationshipDescriptionBuilder relationship( RelationshipType relationshipType, Property... properties )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, Direction.OUTGOING, properties );
        return this;
    }

    @Override
    public RelationshipDescription relationshipConstraints( Range cardinality, Uniqueness uniqueness )
    {
        relationshipConstraints = new RelationshipConstraints( cardinality, uniqueness );
        return new RelationshipDescription( nodes, relationshipInfo, relationshipConstraints );
    }

    @Override
    public RelationshipDescription relationshipConstraints( Range cardinality )
    {
        relationshipConstraints = new RelationshipConstraints( cardinality, Uniqueness.ALLOW_MULTIPLE );
        return new RelationshipDescription( nodes, relationshipInfo, relationshipConstraints );
    }
}
