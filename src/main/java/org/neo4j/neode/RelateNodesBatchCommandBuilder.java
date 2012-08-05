package org.neo4j.neode;

public class RelateNodesBatchCommandBuilder implements UpdateDataset<NodeCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection nodeCollection;
    private final TargetNodesSpecification entities;

    public RelateNodesBatchCommandBuilder( NodeCollection nodeCollection, TargetNodesSpecification entities )
    {
        this.nodeCollection = nodeCollection;
        this.entities = entities;
    }

    @Override
    public NodeCollection update( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( nodeCollection, entities,
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
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( nodeCollection, entities,
                NullEndNodeIdCollector.INSTANCE, batchSize );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}