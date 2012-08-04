package org.neo4j.neode.commands;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.logging.Log;

class RelateToChoiceOfNodesBatchCommand implements BatchCommand<List<NodeCollection>>
{
    private final NodeCollection startNodes;
    private final Commands commands;
    private final int batchSize;

    public RelateToChoiceOfNodesBatchCommand( NodeCollection startNodes, Commands commands, int batchSize )
    {
        this.startNodes = startNodes;
        this.commands = commands;
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
        BatchCommand<NodeCollection> nextCommand = commands.nextCommand( currentNode, random );
        nextCommand.execute( currentNode, db, index, random );
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
        commands.onBegin(log);
    }

    @Override
    public void onEnd( Log log )
    {
        commands.onEnd(log);
    }

    @Override
    public List<NodeCollection> results()
    {
        return commands.results();
    }
}
