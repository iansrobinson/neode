package org.neo4j.neode.commands;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.TraversalDescription;

class TraversalBasedGraphQuery extends GraphQuery
{
    private final TraversalDescription traversal;

    TraversalBasedGraphQuery( TraversalDescription traversal )
    {
        this.traversal = traversal;
    }

    @Override
    Iterable<Node> execute( Node startNode )
    {
        return traversal.traverse( startNode ).nodes();
    }
}
