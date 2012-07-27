package org.neo4j.neode.commands;

import org.neo4j.neode.Dataset;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.commands.interfaces.SetRelationshipDescription;
import org.neo4j.neode.commands.interfaces.UpdateDataset;

public class RelateNodesBatchCommandBuilder implements SetRelationshipDescription, UpdateDataset
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final DomainEntityInfo domainEntityInfo;
    private RelationshipDescription entities;

    public RelateNodesBatchCommandBuilder( DomainEntityInfo domainEntityInfo )
    {
        this.domainEntityInfo = domainEntityInfo;
    }

    @Override
    public UpdateDataset to( RelationshipDescription entities )
    {
        this.entities = entities;
        return this;
    }

    @Override
    public RelateToChoiceOfNodesBatchCommandBuilder to( EntityChoices entityChoices )
    {
        return new RelateToChoiceOfNodesBatchCommandBuilder( domainEntityInfo, entityChoices  );
    }

    @Override
    public DomainEntityInfo update( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( domainEntityInfo, entities,
                new TargetNodeIdCollector(), batchSize );
        return dataset.execute( command );
    }

    @Override
    public DomainEntityInfo update( Dataset dataset )
    {
        return update( dataset, DEFAULT_BATCH_SIZE );
    }

    @Override
    public void updateNoReturn( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( domainEntityInfo, entities,
                NullEndNodeIdCollector.INSTANCE, batchSize );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }
}