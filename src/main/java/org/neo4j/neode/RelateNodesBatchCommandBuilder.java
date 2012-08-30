package org.neo4j.neode;

import org.neo4j.neode.interfaces.UpdateDataset;

class RelateNodesBatchCommandBuilder implements UpdateDataset<NodeCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection sourceNodes;
    private final CreateRelationshipSpecification createRelationshipSpecification;

    RelateNodesBatchCommandBuilder( NodeCollection sourceNodes, CreateRelationshipSpecification createRelationshipSpecification )
    {
        this.sourceNodes = sourceNodes;
        this.createRelationshipSpecification = createRelationshipSpecification;
    }

    @Override
    public NodeCollection update( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodes, createRelationshipSpecification,
                new UniqueNodeIdCollector(), batchSize );
        return dataset.execute( command );
    }

    @Override
    public NodeCollection update( Dataset dataset )
    {
        return update( dataset, DEFAULT_BATCH_SIZE );
    }

    @Override
    public void updateNoReturn( Dataset dataset, int batchSize )
    {

        RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodes, createRelationshipSpecification,
                NullNodeIdCollector.INSTANCE, batchSize );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}