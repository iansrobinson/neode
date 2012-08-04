package org.neo4j.neode.commands;

import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.interfaces.UpdateDataset;

class NodeBatchCommandBuilder implements UpdateDataset<NodeCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 20000;

    private final NodeSpecification nodeSpecification;
    private final int quantity;

    public NodeBatchCommandBuilder( NodeSpecification nodeSpecification, int quantity )
    {
        this.nodeSpecification = nodeSpecification;
        this.quantity = quantity;
    }

    @Override
    public NodeCollection update( Dataset dataset, int batchSize )
    {
        NodeBatchCommand command = new NodeBatchCommand( nodeSpecification, quantity, batchSize,
                new UniqueNodeIdCollector() );
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
        NodeBatchCommand command = new NodeBatchCommand( nodeSpecification, quantity, batchSize,
                NullEndNodeIdCollector.INSTANCE );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}
