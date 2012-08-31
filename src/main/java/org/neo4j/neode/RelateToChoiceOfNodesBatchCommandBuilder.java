package org.neo4j.neode;

import java.util.List;

import org.neo4j.neode.interfaces.UpdateDataset;

class RelateToChoiceOfNodesBatchCommandBuilder implements UpdateDataset<List<NodeCollection>>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection nodeCollection;
    private final ChoiceOfTargetNodesStrategy choiceOfTargetNodesStrategy;

    RelateToChoiceOfNodesBatchCommandBuilder( NodeCollection nodeCollection, ChoiceOfTargetNodesStrategy choiceOfTargetNodesStrategy )
    {
        this.nodeCollection = nodeCollection;
        this.choiceOfTargetNodesStrategy = choiceOfTargetNodesStrategy;
    }

    @Override
    public List<NodeCollection> update( Dataset dataset, int batchSize )
    {
        Commands commands = choiceOfTargetNodesStrategy.createCommandSelector( dataset.db(), nodeCollection, batchSize,
                NodeCollectionFactory.INSTANCE );
        RelateToChoiceOfNodesBatchCommand command =
                new RelateToChoiceOfNodesBatchCommand( nodeCollection, commands, batchSize );
        dataset.execute( command );
        return command.results( dataset.db() );
    }

    @Override
    public List<NodeCollection> update( Dataset dataset )
    {
        return update( dataset, DEFAULT_BATCH_SIZE );
    }

    @Override
    public void updateNoReturn( Dataset dataset, int batchSize )
    {
        Commands commands = choiceOfTargetNodesStrategy.createCommandSelector( dataset.db(), nodeCollection,
                DEFAULT_BATCH_SIZE,
                NodeCollectionFactory.NULL );
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
