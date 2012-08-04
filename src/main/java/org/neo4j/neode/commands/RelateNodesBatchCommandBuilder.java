package org.neo4j.neode.commands;

import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.interfaces.UpdateDataset;

public class RelateNodesBatchCommandBuilder implements UpdateDataset<NodeCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection nodeCollection;
    private final RelationshipSpecification entities;

    public RelateNodesBatchCommandBuilder( NodeCollection nodeCollection, RelationshipSpecification entities )
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