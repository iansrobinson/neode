package org.neo4j.neode.commands;

import java.util.List;

import org.neo4j.neode.NodeCollection;

public class RelateToChoiceOfNodesBatchCommandBuilder
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection nodeCollection;
    private final NodeChoices nodeChoices;

    public RelateToChoiceOfNodesBatchCommandBuilder( NodeCollection nodeCollection, NodeChoices nodeChoices )
    {
        this.nodeCollection = nodeCollection;
        this.nodeChoices = nodeChoices;
    }

    public List<NodeCollection> update( Dataset dataset, int batchSize )
    {
        Commands commands = nodeChoices.createCommandSelector( nodeCollection, batchSize );
        RelateToChoiceOfNodesBatchCommand command = new RelateToChoiceOfNodesBatchCommand( nodeCollection,
                commands, batchSize );
        dataset.execute( command );
        return command.results();
    }

    public List<NodeCollection> update( Dataset dataset )
    {
        Commands commands = nodeChoices.createCommandSelector( nodeCollection,DEFAULT_BATCH_SIZE );
        RelateToChoiceOfNodesBatchCommand command = new RelateToChoiceOfNodesBatchCommand( nodeCollection,
                commands, DEFAULT_BATCH_SIZE );
        dataset.execute( command );
        return command.results();
    }
}
