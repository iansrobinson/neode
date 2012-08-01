package org.neo4j.neode.commands;

import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.interfaces.SetQuantity;
import org.neo4j.neode.commands.interfaces.UpdateDataset;

public class NodeBatchCommandBuilder implements UpdateDataset, SetQuantity
{
    private static final int DEFAULT_BATCH_SIZE = 20000;

    private final NodeSpecification nodeSpecification;
    private int numberOfIterations = 0;

    public NodeBatchCommandBuilder( NodeSpecification nodeSpecification )
    {
        this.nodeSpecification = nodeSpecification;
    }

    @Override
    public UpdateDataset quantity( int value )
    {
        numberOfIterations = value;
        return this;
    }

    @Override
    public NodeCollection update( Dataset dataset, int batchSize )
    {
        NodeBatchCommand command =
                new NodeBatchCommand( nodeSpecification, numberOfIterations, batchSize,
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
        NodeBatchCommand command =
                new NodeBatchCommand( nodeSpecification, numberOfIterations, batchSize,
                        NullEndNodeIdCollector.INSTANCE );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }

}
