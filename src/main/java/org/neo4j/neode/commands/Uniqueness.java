/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands;

import static org.neo4j.kernel.Traversal.expanderForTypes;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.RelationshipType;

public enum Uniqueness implements RelationshipUniqueness
{
    SINGLE_DIRECTION
            {
                public void apply( GraphDatabaseService db, Node firstNode, Node secondNode,
                                   RelationshipType relationshipType, Direction direction )
                {
                    PathFinder<Path> pathPathFinder =
                            GraphAlgoFactory.shortestPath(
                                    expanderForTypes( relationshipType, direction ), 1 );

                    if ( pathPathFinder.findSinglePath( firstNode, secondNode ) != null )
                    {
                        return;
                    }

                    applyUniquenessConstraint( firstNode, secondNode, relationshipType, direction );
                }
            },
    BOTH_DIRECTIONS
            {
                public void apply( GraphDatabaseService db, Node firstNode, Node secondNode,
                                   RelationshipType relationshipType, Direction direction )
                {
                    PathFinder<Path> pathPathFinder = GraphAlgoFactory.shortestPath(
                            expanderForTypes( relationshipType, Direction.BOTH ), 1 );

                    if ( pathPathFinder.findSinglePath( firstNode, secondNode ) != null )
                    {
                        return;
                    }

                    applyUniquenessConstraint( firstNode, secondNode, relationshipType, direction );
                }
            },
    ALLOW_MULTIPLE
            {
                public void apply( GraphDatabaseService db, Node firstNode, Node secondNode,
                                   RelationshipType relationshipType, Direction direction )
                {
                    applyUniquenessConstraint( firstNode, secondNode, relationshipType, direction );
                }
            };

    private static void applyUniquenessConstraint( Node firstNode, Node secondNode,
                                                   RelationshipType relationshipType, Direction direction )
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


}
