/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;


import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.commands.interfaces.SetRelationshipConstraints;
import org.neo4j.neode.commands.interfaces.SetRelationshipInfo;
import org.neo4j.neode.numbergenerators.Range;
import org.neo4j.neode.properties.Property;

public abstract class NodeFinder implements SetRelationshipInfo, SetRelationshipConstraints
{
    private RelationshipInfo relationshipInfo;
    private RelationshipConstraints relationshipConstraints;

    abstract Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random );

    abstract String entityName();

    @Override
    public SetRelationshipConstraints relationship( RelationshipType relationshipType, Direction direction,
                                                    Property... properties )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, direction, properties );
        return this;
    }

    @Override
    public SetRelationshipConstraints relationship( RelationshipType relationshipType, Property... properties )
    {
        relationshipInfo = new RelationshipInfo( relationshipType, Direction.OUTGOING, properties );
        return this;
    }

    @Override
    public RelationshipSpecification relationshipConstraints( Range cardinality,
                                                            RelationshipUniqueness relationshipUniqueness )
    {
        relationshipConstraints = new RelationshipConstraints( cardinality, relationshipUniqueness );
        return new RelationshipSpecification( this, relationshipInfo, relationshipConstraints );
    }

    @Override
    public RelationshipSpecification relationshipConstraints( Range cardinality )
    {
        relationshipConstraints = new RelationshipConstraints( cardinality, RelationshipUniqueness.ALLOW_MULTIPLE );
        return new RelationshipSpecification( this, relationshipInfo, relationshipConstraints );
    }
}
