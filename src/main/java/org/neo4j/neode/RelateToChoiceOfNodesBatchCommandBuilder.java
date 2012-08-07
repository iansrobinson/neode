package org.neo4j.neode;

import java.util.List;

class RelateToChoiceOfNodesBatchCommandBuilder implements UpdateDataset<List<NodeCollection>>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection nodeCollection;
    private final TargetNodesSpecificationsChoices targetNodesSpecificationsChoices;

    RelateToChoiceOfNodesBatchCommandBuilder( NodeCollection nodeCollection, TargetNodesSpecificationsChoices targetNodesSpecificationsChoices )
    {
        this.nodeCollection = nodeCollection;
        this.targetNodesSpecificationsChoices = targetNodesSpecificationsChoices;
    }

    @Override
    public List<NodeCollection> update( Dataset dataset, int batchSize )
    {
        Commands commands = targetNodesSpecificationsChoices.createCommandSelector( nodeCollection, batchSize,
                new UniqueNodeIdCollectorFactory() );
        RelateToChoiceOfNodesBatchCommand command =
                new RelateToChoiceOfNodesBatchCommand( nodeCollection, commands, batchSize );
        dataset.execute( command );
        return command.results();
    }

    @Override
    public List<NodeCollection> update( Dataset dataset )
    {
        return update( dataset, DEFAULT_BATCH_SIZE );
    }

    @Override
    public void updateNoReturn( Dataset dataset, int batchSize )
    {
        Commands commands = targetNodesSpecificationsChoices.createCommandSelector( nodeCollection, DEFAULT_BATCH_SIZE,
                new NullNodeIdCollectorFactory() );
        RelateToChoiceOfNodesBatchCommand command =
                new RelateToChoiceOfNodesBatchCommand( nodeCollection, commands, batchSize );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}
