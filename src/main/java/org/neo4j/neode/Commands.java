package org.neo4j.neode;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.logging.Log;

class Commands
{
    private final List<BatchCommand<NodeCollection>> commands;
    private final CommandSelectionStrategy commandSelectionStrategy;

    Commands( List<BatchCommand<NodeCollection>> commands, CommandSelectionStrategy commandSelectionStrategy )
    {
        this.commands = commands;
        this.commandSelectionStrategy = commandSelectionStrategy;
    }

    public BatchCommand<NodeCollection> nextCommand()
    {
        return commandSelectionStrategy.nextCommand( commands );
    }

    public List<NodeCollection> results( GraphDatabaseService db )
    {
        List<NodeCollection> results = new ArrayList<NodeCollection>();
        for ( BatchCommand<NodeCollection> command : commands )
        {
            results.add( command.results( db ) );
        }
        return results;
    }

    public void onBegin( Log log )
    {
        for ( BatchCommand<NodeCollection> command : commands )
        {
            log.write( String.format( "      [%s]", command.shortDescription() ) );
            command.onBegin( log );
        }
    }

    public void onEnd( Log log )
    {
        for ( BatchCommand<NodeCollection> command : commands )
        {
            log.write( String.format( "      [%s]", command.shortDescription() ) );
            command.onEnd( log );
        }
    }
}
