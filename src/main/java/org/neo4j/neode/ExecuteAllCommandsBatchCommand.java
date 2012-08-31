package org.neo4j.neode;

import org.neo4j.neode.logging.Log;

public class ExecuteAllCommandsBatchCommand implements BatchCommand<NodeCollection>
{
    private final Iterable<BatchCommand<NodeCollection>> commands;

    public ExecuteAllCommandsBatchCommand( Iterable<BatchCommand<NodeCollection>> commands )
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
    public void execute( int iteration )
    {
        for ( BatchCommand<NodeCollection> command : commands )
        {
            command.execute( iteration );
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
    public NodeCollection results()
    {
        throw new IllegalAccessError();
    }
}
