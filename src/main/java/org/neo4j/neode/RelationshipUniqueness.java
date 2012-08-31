/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

public enum RelationshipUniqueness
{
    SINGLE_DIRECTION
            {
                public Relationship createRelationship( Node firstNode, Node secondNode,
                                                        RelationshipInfo relationshipInfo, int iteration )
                {
                    PathFinder<Path> pathPathFinder =
                            GraphAlgoFactory.shortestPath( relationshipInfo.expander(), 1 );

                    if ( pathPathFinder.findSinglePath( firstNode, secondNode ) != null )
                    {
                        return null;
                    }

                    return relationshipInfo.createRelationship( firstNode, secondNode, iteration );
                }
            },
    BOTH_DIRECTIONS
            {
                public Relationship createRelationship( Node firstNode, Node secondNode,
                                                        RelationshipInfo relationshipInfo, int iteration )
                {
                    PathFinder<Path> pathPathFinder = GraphAlgoFactory.shortestPath(
                            relationshipInfo.expanderIgnoreDirection(), 1 );

                    if ( pathPathFinder.findSinglePath( firstNode, secondNode ) != null )
                    {
                        return null;
                    }

                    return relationshipInfo.createRelationship( firstNode, secondNode, iteration );
                }
            },
    ALLOW_MULTIPLE
            {
                public Relationship createRelationship( Node firstNode, Node secondNode,
                                                        RelationshipInfo relationshipInfo, int iteration )
                {
                    return relationshipInfo.createRelationship( firstNode, secondNode, iteration );
                }
            };

    public abstract Relationship createRelationship( Node firstNode, Node secondNode, RelationshipInfo relationshipInfo,
                                                     int iteration );


}
