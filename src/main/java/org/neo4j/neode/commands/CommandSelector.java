package org.neo4j.neode.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;

public class CommandSelector
{
    private final List<BatchCommand<DomainEntityInfo>> commands;
    private final CommandSelectionStrategy commandSelectionStrategy;

    public CommandSelector( List<BatchCommand<DomainEntityInfo>> commands,
                            CommandSelectionStrategy commandSelectionStrategy )
    {
        this.commands = commands;
        this.commandSelectionStrategy = commandSelectionStrategy;
    }

    public BatchCommand<DomainEntityInfo> nextCommand( Node currentNode, Random random )
    {
        return commandSelectionStrategy.nextCommand( currentNode, commands, random );
    }

    public List<DomainEntityInfo> results()
    {
        List<DomainEntityInfo> results = new ArrayList<DomainEntityInfo>();
        for ( BatchCommand<DomainEntityInfo> command : commands )
        {
            results.add( command.results() );
        }
        return results;
    }
}
