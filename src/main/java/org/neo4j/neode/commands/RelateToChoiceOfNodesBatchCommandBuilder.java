package org.neo4j.neode.commands;

import java.util.List;

import org.neo4j.neode.NodeCollection;

public class RelateToChoiceOfNodesBatchCommandBuilder
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection nodeCollection;
    private final EntityChoices entityChoices;

    public RelateToChoiceOfNodesBatchCommandBuilder( NodeCollection nodeCollection, EntityChoices entityChoices )
    {
        this.nodeCollection = nodeCollection;
        this.entityChoices = entityChoices;
    }

    public List<NodeCollection> update( Dataset dataset, int batchSize )
    {
        Commands commands = entityChoices.createCommandSelector( nodeCollection, batchSize );
        RelateToChoiceOfNodesBatchCommand command = new RelateToChoiceOfNodesBatchCommand( nodeCollection,
                commands, batchSize );
        dataset.execute( command );
        return command.results();
    }

    public List<NodeCollection> update( Dataset dataset )
    {
        Commands commands = entityChoices.createCommandSelector( nodeCollection,DEFAULT_BATCH_SIZE );
        RelateToChoiceOfNodesBatchCommand command = new RelateToChoiceOfNodesBatchCommand( nodeCollection,
                commands, DEFAULT_BATCH_SIZE );
        dataset.execute( command );
        return command.results();
    }
}
