package org.neo4j.neode;

import java.util.ArrayList;

import org.neo4j.neode.interfaces.UpdateDataset;

class RelateNodesBatchCommandBuilder implements UpdateDataset<NodeIdCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeIdCollection sourceNodeIds;
    private final TargetNodes targetNodes;

    RelateNodesBatchCommandBuilder( NodeIdCollection sourceNodeIds, TargetNodes targetNodes )
    {
        this.sourceNodeIds = sourceNodeIds;
        this.targetNodes = targetNodes;
    }

    @Override
    public NodeIdCollection update( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodeIds, targetNodes,
               targetNodes.newNodeIdCollection( new ArrayList<Long>() ), batchSize );
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

        RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodeIds, targetNodes,
                NodeIdCollection.NULL, batchSize );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}