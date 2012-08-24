package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class QueryBasedGetExistingNodes extends RelationshipBuilder
{
    private final GraphQuery query;

    public QueryBasedGetExistingNodes( GraphQuery query )
    {
        this.query = query;
    }

    @Override
    Iterable<Node> getNodes( int quantity, GraphDatabaseService db, Node currentNode, Random random )
    {
        return query.execute( db, currentNode );
    }

    @Override
    String label()
    {
        return "";
    }
}
