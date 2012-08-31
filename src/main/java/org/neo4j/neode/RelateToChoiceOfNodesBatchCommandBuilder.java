package org.neo4j.neode;

import java.util.List;

import org.neo4j.neode.interfaces.UpdateDataset;

class RelateToChoiceOfNodesBatchCommandBuilder implements UpdateDataset<List<NodeIdCollection>>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeIdCollection nodeIdCollection;
    private final CreateRelationshipSpecificationChoices createRelationshipSpecificationChoices;

    RelateToChoiceOfNodesBatchCommandBuilder( NodeIdCollection nodeIdCollection, CreateRelationshipSpecificationChoices createRelationshipSpecificationChoices )
    {
        this.nodeIdCollection = nodeIdCollection;
        this.createRelationshipSpecificationChoices = createRelationshipSpecificationChoices;
    }

    @Override
    public List<NodeIdCollection> update( Dataset dataset, int batchSize )
    {
        Commands commands = createRelationshipSpecificationChoices.createCommandSelector( nodeIdCollection, batchSize,
                NodeIdCollectionFactory.INSTANCE );
        RelateToChoiceOfNodesBatchCommand command =
                new RelateToChoiceOfNodesBatchCommand( nodeIdCollection, commands, batchSize );
        dataset.execute( command );
        return command.results();
    }

    @Override
    public List<NodeIdCollection> update( Dataset dataset )
    {
        return update( dataset, DEFAULT_BATCH_SIZE );
    }

    @Override
    public void updateNoReturn( Dataset dataset, int batchSize )
    {
        Commands commands = createRelationshipSpecificationChoices.createCommandSelector( nodeIdCollection,
                DEFAULT_BATCH_SIZE,
                NodeIdCollectionFactory.NULL );
        RelateToChoiceOfNodesBatchCommand command =
                new RelateToChoiceOfNodesBatchCommand( nodeIdCollection, commands, batchSize );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}
