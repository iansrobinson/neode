package org.neo4j.neode;

import java.util.HashSet;

import org.neo4j.neode.interfaces.UpdateDataset;

class RelateNodesBatchCommandBuilder implements UpdateDataset<NodeCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection sourceNodes;
    private final TargetNodesStrategy targetNodesStrategy;

    RelateNodesBatchCommandBuilder( NodeCollection sourceNodes, TargetNodesStrategy targetNodesStrategy )
    {
        this.sourceNodes = sourceNodes;
        this.targetNodesStrategy = targetNodesStrategy;
    }

    @Override
    public NodeCollection update( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodes, targetNodesStrategy,
                targetNodesStrategy.newNodeCollection( dataset.db(), new HashSet<Long>() ), batchSize );
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

        RelateNodesBatchCommand command = new RelateNodesBatchCommand( sourceNodes, targetNodesStrategy,
                NodeCollection.NULL, batchSize );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}