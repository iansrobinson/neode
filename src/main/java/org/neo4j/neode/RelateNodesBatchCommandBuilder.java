package org.neo4j.neode;

import org.neo4j.neode.interfaces.UpdateDataset;

class RelateNodesBatchCommandBuilder implements UpdateDataset<NodeIdCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeIdCollection sourceNodeIds;
    private final CreateRelationshipSpecification createRelationshipSpecification;

    RelateNodesBatchCommandBuilder( NodeIdCollection sourceNodeIds, CreateRelationshipSpecification createRelationshipSpecification )
    {
        this.sourceNodeIds = sourceNodeIds;
        this.createRelationshipSpecification = createRelationshipSpecification;
    }

    @Override
    public NodeIdCollection update( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodeIds, createRelationshipSpecification,
                new UniqueNodeIdCollector(), batchSize );
        return dataset.execute( command );
    }

    @Override
    public NodeIdCollection update( Dataset dataset )
    {
        return update( dataset, DEFAULT_BATCH_SIZE );
    }

    @Override
    public void updateNoReturn( Dataset dataset, int batchSize )
    {

        RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodeIds, createRelationshipSpecification,
                NullNodeIdCollector.INSTANCE, batchSize );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}