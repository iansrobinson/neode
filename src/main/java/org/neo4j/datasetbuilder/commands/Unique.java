package org.neo4j.datasetbuilder.commands;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.kernel.Traversal;

public class Unique implements UniquenessStrategy
{
    public static UniquenessStrategy unique()
        {
            return new Unique();
        }

        private Unique()
        {
        }

        @Override
        public void apply( GraphDatabaseService db, Node firstNode, Node secondNode, RelationshipType relationshipType,
                           Direction direction )
        {
            PathFinder<Path> pathPathFinder = GraphAlgoFactory.shortestPath( Traversal.expanderForTypes(
                    relationshipType, direction ), 1 );

            if (pathPathFinder.findSinglePath( firstNode, secondNode ) != null)
            {
                return;
            }

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
        return "Unique";
    }
}
