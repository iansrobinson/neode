package org.neo4j.neode.finders;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.TraversalDescription;

public class TraversalBasedQuery implements Query
{
    public static Query traversal( TraversalDescription traversalDescription )
    {
        return new TraversalBasedQuery( traversalDescription );
    }
    private final TraversalDescription traversal;

    public TraversalBasedQuery( TraversalDescription traversal )
    {
        this.traversal = traversal;
    }

    @Override
    public Iterable<Node> execute( Node startNode )
    {
        return traversal.traverse( startNode ).nodes();
    }
}
