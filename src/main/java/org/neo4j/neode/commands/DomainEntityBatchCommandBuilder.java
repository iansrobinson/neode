package org.neo4j.neode.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.commands.interfaces.Update;
import org.neo4j.neode.logging.Log;

public class DomainEntityBatchCommandBuilder implements Update
{
    private static final int DEFAULT_BATCH_SIZE = 20000;

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

    @Override
    public DomainEntityInfo update( Dataset dataset, int batchSize )
    {
        DomainEntityBatchCommand command =
                new DomainEntityBatchCommand( domainEntity, numberOfIterations, batchSize, true );
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
        DomainEntityBatchCommand command =
                new DomainEntityBatchCommand( domainEntity, numberOfIterations, batchSize, false );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }

    private static class DomainEntityBatchCommand implements BatchCommand
    {
        private final DomainEntity domainEntity;
        private final int numberOfIterations;
        private final int batchSize;
        private final List<Long> nodeIds;
        private final boolean captureNodeIds;

        public DomainEntityBatchCommand( DomainEntity domainEntity, int numberOfIterations,
                                         int batchSize, boolean captureNodeIds )
        {
            this.domainEntity = domainEntity;
            this.numberOfIterations = numberOfIterations;
            this.batchSize = batchSize;
            this.captureNodeIds = captureNodeIds;
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
            Long nodeId = domainEntity.build( db, index );
            if ( captureNodeIds )
            {
                nodeIds.add( nodeId );
            }
        }

        @Override
        public String description()
        {
            return "Creating '" + shortDescription() + "' nodes.";
        }

        @Override
        public String shortDescription()
        {
            return domainEntity.entityName();
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
