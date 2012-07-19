package org.neo4j.datasetbuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.neo4j.datasetbuilder.test.Db;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;

public class BatchCommandExecutorTest
{
    @Test
    public void shouldExecuteBatchesInSeparateTransactions() throws Exception
    {
        // given
        TransactionCounter transactionCounter = new TransactionCounter();

        GraphDatabaseService db = Db.impermanentDb();
        db.registerTransactionEventHandler( transactionCounter );

        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );
        DummyBatchCommand command = new DummyBatchCommand( 10, 2 );

        // when
        executor.execute( command );

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

        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );
        DummyBatchCommand command = new DummyBatchCommand( 10, 3 );

        // when
        executor.execute( command );

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

        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );
        DummyBatchCommand command = new DummyBatchCommand( 3, 5 );

        // when
        executor.execute( command );

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


    private class DummyBatchCommand implements BatchCommand<Object>
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
        public void execute( GraphDatabaseService db, int index )
        {
            db.createNode();
            callCount++;
        }

        @Override
        public String description()
        {
            return "Dummy batch command";
        }

        public int callCount()
        {
            return callCount;
        }

        @Override
        public Object value()
        {
            return null;
        }
    }
}
