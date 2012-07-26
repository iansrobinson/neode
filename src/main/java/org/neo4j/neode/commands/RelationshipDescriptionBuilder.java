package org.neo4j.neode.commands;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.commands.interfaces.CreateRelationshipConstraints;
import org.neo4j.neode.commands.interfaces.CreateRelationshipInfo;
import org.neo4j.neode.finders.NodeFinder;
import org.neo4j.neode.numbergenerators.Range;

public class RelationshipDescriptionBuilder implements CreateRelationshipInfo, CreateRelationshipConstraints
{
    private final NodeFinder nodes;
    private RelationshipInfo relationshipInfo;
    private RelationshipConstraints relationshipConstraints;

    public RelationshipDescriptionBuilder( NodeFinder nodes )
    {
        this.nodes = nodes;
    }

    @Override
    public RelationshipDescriptionBuilder relationship( RelationshipType relationshipType, Direction direction )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, direction );
        return this;
    }

    @Override
    public RelationshipDescriptionBuilder relationship( RelationshipType relationshipType )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, Direction.OUTGOING );
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
