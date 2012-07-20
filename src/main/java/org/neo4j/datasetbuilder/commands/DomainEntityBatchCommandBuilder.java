package org.neo4j.datasetbuilder.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.BatchCommand;
import org.neo4j.datasetbuilder.BatchCommandExecutor;
import org.neo4j.datasetbuilder.Log;
import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.graphdb.GraphDatabaseService;

public class DomainEntityBatchCommandBuilder
{

    public static DomainEntityBatchCommandBuilder createEntities( DomainEntityBuilder domainEntityBuilder )
    {
        return new DomainEntityBatchCommandBuilder( domainEntityBuilder );
    }

    private DomainEntityBuilder domainEntityBuilder;
    private int numberOfIterations = 0;
    private int batchSize = 10000;

    private DomainEntityBatchCommandBuilder( DomainEntityBuilder domainEntityBuilder )
    {
        this.domainEntityBuilder = domainEntityBuilder;
    }

    public DomainEntityBatchCommandBuilder quantity( int value )
    {
        numberOfIterations = value;
        return this;
    }

    public DomainEntityBatchCommandBuilder batchSize( int value )
    {
        batchSize = value;
        return this;
    }

    public DomainEntityInfo execute( BatchCommandExecutor executor )
    {
        DomainEntityBatchCommand command =
                new DomainEntityBatchCommand( domainEntityBuilder, numberOfIterations, batchSize );
        return executor.execute( command );
    }

    private static class DomainEntityBatchCommand implements BatchCommand
    {
        private final DomainEntityBuilder domainEntityBuilder;
        private final int numberOfIterations;
        private final int batchSize;
        private final List<Long> nodeIds;

        public DomainEntityBatchCommand( DomainEntityBuilder domainEntityBuilder, int numberOfIterations,
                                         int batchSize )
        {
            this.domainEntityBuilder = domainEntityBuilder;
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
            nodeIds.add( domainEntityBuilder.build( db, index ) );
        }

        @Override
        public String description()
        {
            return "Creating '" + domainEntityBuilder.entityName() + "' nodes.";
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
            return new DomainEntityInfo(domainEntityBuilder.entityName(), nodeIds );
        }
    }
}
