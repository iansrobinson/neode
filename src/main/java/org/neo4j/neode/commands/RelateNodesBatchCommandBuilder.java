package org.neo4j.neode.commands;

import java.util.List;

import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.interfaces.SetRelationshipDescription;
import org.neo4j.neode.commands.interfaces.UpdateDataset;

public class RelateNodesBatchCommandBuilder implements SetRelationshipDescription, UpdateDataset<NodeCollection>
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final NodeCollection nodeCollection;
    private RelationshipSpecification entities;

    public RelateNodesBatchCommandBuilder( NodeCollection nodeCollection )
    {
        this.nodeCollection = nodeCollection;
    }

    @Override
    public UpdateDataset<NodeCollection> to( RelationshipSpecification entities )
    {
        this.entities = entities;
        return this;
    }

    @Override
    public UpdateDataset<List<NodeCollection>> to( NodeChoices nodeChoices )
    {
        return new RelateToChoiceOfNodesBatchCommandBuilder( nodeCollection, nodeChoices );
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