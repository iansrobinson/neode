package org.neo4j.neode;

import java.util.List;

import org.neo4j.neode.logging.Log;

class RelateToChoiceOfNodesBatchCommand implements BatchCommand<List<NodeCollection>>
{
    private final NodeCollection startNodes;
    private final Commands commands;
    private final int batchSize;

    RelateToChoiceOfNodesBatchCommand( NodeCollection startNodes, Commands commands, int batchSize )
    {
        this.startNodes = startNodes;
        this.commands = commands;
        this.batchSize = batchSize;
    }

    @Override
    public int numberOfIterations()
    {
        return startNodes.size();
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( int iteration )
    {
        BatchCommand<NodeCollection> nextCommand = commands.nextCommand();
        nextCommand.execute( iteration );
    }

    @Override
    public String description()
    {
        return "Creating choice of relationships.";
    }

    @Override
    public String shortDescription()
    {
        return "Create choice of relationships";
    }

    @Override
    public void onBegin( Log log )
    {
        commands.onBegin( log );
    }

    @Override
    public void onEnd( Log log )
    {
        commands.onEnd( log );
    }

    @Override
    public List<NodeCollection> results()
    {
        return commands.results();
    }
}
