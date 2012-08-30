package org.neo4j.neode;

import org.neo4j.neode.interfaces.UpdateDataset;

class CreateNodesBatchCommandBuilder implements UpdateDataset<NodeIdCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 20000;

    private final NodeSpecification nodeSpecification;
    private final int quantity;

    CreateNodesBatchCommandBuilder( NodeSpecification nodeSpecification, int quantity )
    {
        this.nodeSpecification = nodeSpecification;
        this.quantity = quantity;
    }

    @Override
    public NodeIdCollection update( Dataset dataset, int batchSize )
    {
        NodeIdCollection nodeIdCollection = nodeSpecification.emptyNodeIdCollection( quantity );
        CreateNodesBatchCommand command = new CreateNodesBatchCommand( nodeSpecification, quantity, nodeIdCollection,
                batchSize);
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
        CreateNodesBatchCommand command = new CreateNodesBatchCommand( nodeSpecification, quantity,
                NodeIdCollection.NULL, batchSize);
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}
