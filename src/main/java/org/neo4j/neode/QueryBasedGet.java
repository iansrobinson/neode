package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class QueryBasedGet extends NodeFinder
{
    private final GraphQuery query;

    public QueryBasedGet( GraphQuery query )
    {
        this.query = query;
    }

    @Override
    Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        return query.execute( currentNode );
    }

    @Override
    String label()
    {
        return "";
    }
}
