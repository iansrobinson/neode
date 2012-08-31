package org.neo4j.neode;

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
    public Iterable<Node> getTargetNodes( int quantity, GraphDatabaseService db, Node currentNode )
    {
        return query.execute( currentNode );
    }

    @Override
    public String label()
    {
        return "";
    }
}
