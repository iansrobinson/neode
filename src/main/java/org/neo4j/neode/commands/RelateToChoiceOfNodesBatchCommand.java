package org.neo4j.neode.commands;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.logging.Log;

public class RelateToChoiceOfNodesBatchCommand implements BatchCommand<List<DomainEntityInfo>>
{
    private final DomainEntityInfo startNodes;
    private final CommandSelector commandSelector;
    private final int batchSize;

    public RelateToChoiceOfNodesBatchCommand( DomainEntityInfo startNodes, CommandSelector commandSelector,
                                              int batchSize )
    {
        this.startNodes = startNodes;
        this.commandSelector = commandSelector;
        this.batchSize = batchSize;
    }

    @Override
    public int numberOfIterations()
    {
        return startNodes.nodeIds().size();
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( GraphDatabaseService db, int index, Random random )
    {
        Node currentNode = db.getNodeById( startNodes.nodeIds().get( index ) );
        execute( currentNode, db, index, random );
    }

    @Override
    public void execute( Node currentNode, GraphDatabaseService db, int index, Random random )
    {
        BatchCommand<DomainEntityInfo> nextCommand = commandSelector.nextCommand( currentNode, random );
        nextCommand.execute( currentNode, db, index, random );
    }

    @Override
    public String description()
    {
        return "TODO DESCRIPTION";
    }

    @Override
    public String shortDescription()
    {
        return "TODO SHORT DESCRIPTION";
    }

    @Override
    public void onBegin( Log log )
    {
    }

    @Override
    public void onEnd( Log log )
    {
    }

    @Override
    public List<DomainEntityInfo> results()
    {
        return commandSelector.results();
    }
}
