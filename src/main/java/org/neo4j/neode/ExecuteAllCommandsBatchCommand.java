package org.neo4j.neode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.logging.Log;

public class ExecuteAllCommandsBatchCommand implements BatchCommand<NodeIdCollection>
{
    private final Iterable<BatchCommand<NodeIdCollection>> commands;

    public ExecuteAllCommandsBatchCommand( Iterable<BatchCommand<NodeIdCollection>> commands )
    {
        this.commands = commands;
    }

    @Override
    public int numberOfIterations()
    {
        throw new IllegalAccessError();
    }

    @Override
    public int batchSize()
    {
        throw new IllegalAccessError();
    }

    @Override
    public void execute( GraphDatabaseService db, int iteration )
    {
        for ( BatchCommand<NodeIdCollection> command : commands )
        {
            command.execute( db, iteration );
        }
    }

    @Override
    public String description()
    {
        throw new IllegalAccessError();
    }

    @Override
    public String shortDescription()
    {
        throw new IllegalAccessError();
    }

    @Override
    public void onBegin( Log log )
    {
        throw new IllegalAccessError();
    }

    @Override
    public void onEnd( Log log )
    {
        throw new IllegalAccessError();
    }

    @Override
    public NodeIdCollection results()
    {
        throw new IllegalAccessError();
    }
}
