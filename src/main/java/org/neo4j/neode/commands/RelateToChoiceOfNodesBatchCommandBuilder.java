package org.neo4j.neode.commands;

import java.util.List;

import org.neo4j.neode.DomainEntityInfo;

public class RelateToChoiceOfNodesBatchCommandBuilder
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private final DomainEntityInfo domainEntityInfo;
    private final EntityChoices entityChoices;

    public RelateToChoiceOfNodesBatchCommandBuilder( DomainEntityInfo domainEntityInfo, EntityChoices entityChoices )
    {
        this.domainEntityInfo = domainEntityInfo;
        this.entityChoices = entityChoices;
    }

    public List<DomainEntityInfo> update( Dataset dataset, int batchSize )
    {
        Commands commands = entityChoices.createCommandSelector( domainEntityInfo, batchSize );
        RelateToChoiceOfNodesBatchCommand command = new RelateToChoiceOfNodesBatchCommand( domainEntityInfo,
                commands, batchSize );
        dataset.execute( command );
        return command.results();
    }

    public List<DomainEntityInfo> update( Dataset dataset )
    {
        Commands commands = entityChoices.createCommandSelector( domainEntityInfo,DEFAULT_BATCH_SIZE );
        RelateToChoiceOfNodesBatchCommand command = new RelateToChoiceOfNodesBatchCommand( domainEntityInfo,
                commands, DEFAULT_BATCH_SIZE );
        dataset.execute( command );
        return command.results();
    }
}
