package org.neo4j.neode.commands;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.logging.Log;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.test.Db;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;

public class DatasetTest
{
    @Test
    public void shouldExecuteBatchesInSeparateTransactions() throws Exception
    {
        // given
        TransactionCounter transactionCounter = new TransactionCounter();

        GraphDatabaseService db = Db.impermanentDb();
        db.registerTransactionEventHandler( transactionCounter );

        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        DummyBatchCommand command = new DummyBatchCommand( 10, 2 );

        // when
        dataset.execute( command );

        // then
        assertEquals( 5, transactionCounter.beforeCommitCount() );
        assertEquals( 5, transactionCounter.afterCommitCount() );
        assertEquals( command.numberOfIterations(), command.callCount() );
    }

    @Test
    public void shouldCompleteTransactionsForDanglingBatches() throws Exception
    {
        // given
        TransactionCounter transactionCounter = new TransactionCounter();

        GraphDatabaseService db = Db.impermanentDb();
        db.registerTransactionEventHandler( transactionCounter );

        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        DummyBatchCommand command = new DummyBatchCommand( 10, 3 );

        // when
        dataset.execute( command );

        // then
        assertEquals( 4, transactionCounter.beforeCommitCount() );
        assertEquals( 4, transactionCounter.afterCommitCount() );
        assertEquals( command.numberOfIterations(), command.callCount() );
    }

    @Test
    public void shouldCompleteTransactionsWhenNumberOfIterationsIsSmallerThanBatchSize() throws Exception
    {
        // given
        TransactionCounter transactionCounter = new TransactionCounter();

        GraphDatabaseService db = Db.impermanentDb();
        db.registerTransactionEventHandler( transactionCounter );

        DatasetManager executor = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = executor.newDataset( "Test" );
        DummyBatchCommand command = new DummyBatchCommand( 3, 5 );

        // when
        dataset.execute( command );

        // then
        assertEquals( 1, transactionCounter.beforeCommitCount() );
        assertEquals( 1, transactionCounter.afterCommitCount() );
        assertEquals( command.numberOfIterations(), command.callCount() );
    }

    private class TransactionCounter implements TransactionEventHandler<Object>
    {
        private int beforeCommitCount = 0;
        private int afterCommitCount = 0;

        @Override
        public Object beforeCommit( TransactionData data ) throws Exception
        {
            beforeCommitCount++;
            return null;
        }

        @Override
        public void afterCommit( TransactionData data, Object state )
        {
            afterCommitCount++;
        }

        @Override
        public void afterRollback( TransactionData data, Object state )
        {
        }

        public int beforeCommitCount()
        {
            return beforeCommitCount;
        }

        public int afterCommitCount()
        {
            return afterCommitCount;
        }
    }


    private class DummyBatchCommand implements BatchCommand<NodeCollection>
    {
        private final int numberOfIterations;
        private final int batchSize;
        private int callCount = 0;

        private DummyBatchCommand( int numberOfIterations, int batchSize )
        {
            this.numberOfIterations = numberOfIterations;
            this.batchSize = batchSize;
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
        public void execute( GraphDatabaseService db, int iteration, Random random )
        {
            db.createNode();
            callCount++;
        }

        @Override
        public void execute( Node firstNode, GraphDatabaseService db, int iteration, Random random )
        {
            execute( db, iteration, random );
        }

        @Override
        public String description()
        {
            return "Dummy batch command";
        }

        @Override
        public String shortDescription()
        {
            return "";
        }

        @Override
        public void onBegin( Log log )
        {
        }

        @Override
        public void onEnd( Log log )
        {
        }

        @Override
        public NodeCollection results()
        {
            return null;
        }

        public int callCount()
        {
            return callCount;
        }
    }
}
