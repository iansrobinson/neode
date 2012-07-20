package org.neo4j.datasetbuilder.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.BatchCommand;
import org.neo4j.datasetbuilder.BatchCommandExecutor;
import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.Log;
import org.neo4j.graphdb.GraphDatabaseService;

public class DomainEntityBatchCommandBuilder
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    public static DomainEntityBatchCommandBuilder createEntities( DomainEntity domainEntity )
    {
        return new DomainEntityBatchCommandBuilder( domainEntity );
    }

    private DomainEntity domainEntity;
    private int numberOfIterations = 0;

    private DomainEntityBatchCommandBuilder( DomainEntity domainEntity )
    {
        this.domainEntity = domainEntity;
    }

    public DomainEntityBatchCommandBuilder quantity( int value )
    {
        numberOfIterations = value;
        return this;
    }

    public DomainEntityInfo execute( BatchCommandExecutor executor, int batchSize )
    {
        DomainEntityBatchCommand command =
                new DomainEntityBatchCommand( domainEntity, numberOfIterations, batchSize );
        return executor.execute( command );
    }

    public DomainEntityInfo execute( BatchCommandExecutor executor )
    {
        return execute( executor, DEFAULT_BATCH_SIZE );
    }

    private static class DomainEntityBatchCommand implements BatchCommand
    {
        private final DomainEntity domainEntity;
        private final int numberOfIterations;
        private final int batchSize;
        private final List<Long> nodeIds;

        public DomainEntityBatchCommand( DomainEntity domainEntity, int numberOfIterations,
                                         int batchSize )
        {
            this.domainEntity = domainEntity;
            this.numberOfIterations = numberOfIterations;
            this.batchSize = batchSize;
            nodeIds = new ArrayList<Long>();
        }

        @Override
        public int numberOfIterations()
        {
            return numberOfIterations;
        }

        @Override
        public int batchSize()
        {
            return batchSize;
        }

        @Override
        public void execute( GraphDatabaseService db, int index, Random random )
        {
            nodeIds.add( domainEntity.build( db, index ) );
        }

        @Override
        public String description()
        {
            return "Creating '" + domainEntity.entityName() + "' nodes.";
        }

        @Override
        public void onBegin( Log log )
        {
            // Do nothing
        }

        @Override
        public void onEnd( Log log )
        {
            // Do nothing
        }

        @Override
        public DomainEntityInfo results()
        {
            return new DomainEntityInfo( domainEntity.entityName(), nodeIds );
        }
    }
}
