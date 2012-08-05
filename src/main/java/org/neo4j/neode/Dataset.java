package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.neode.logging.Log;

public class Dataset
{
    private final String description;
    private final GraphDatabaseService db;
    private final Log log;
    private final Random random;
    private final long runStartTime;

    Dataset( String description, GraphDatabaseService db, Log log, Random random )
    {
        this.description = description;
        this.db = db;
        this.log = log;
        this.random = random;
        runStartTime = System.nanoTime();
        log.write( String.format( "Begin [%s]\n", description ) );
    }

    <T> T execute( BatchCommand<T> command )
    {
        long startTime = System.nanoTime();

        log.write( String.format( "Begin [%s Iterations: %s, BatchSize: %s]",
                command.description(), command.numberOfIterations(), command.batchSize() ) );
        command.onBegin( log );
        for ( int iteration = 0; iteration < command.numberOfIterations(); iteration += command.batchSize() )
        {
            doExecute( iteration, command, startTime );
        }
        command.onEnd( log );
        log.write( String.format( "End   [%s] %s\n", command.description(), elapsedTime( startTime ) ) );

        return command.results();
    }

    public void end()
    {
        log.write( String.format( "Store [%s]\n\nEnd   [%s] %s",
                ((EmbeddedGraphDatabase) db).getStoreDir(), description, elapsedTime( runStartTime ) ) );
    }

    private void doExecute( int startIteration, BatchCommand command, long startTime )
    {
        log.write( String.format( "      [Beginning " + startIteration + " of " + command.numberOfIterations() +
                ", '%s'] %s", command.shortDescription(), elapsedTime( startTime ) ) );

        Transaction tx = db.beginTx();
        try
        {
            for ( int iteration = startIteration; iterationIsInRange( startIteration, command, iteration ); iteration++ )
            {
                command.execute( db, iteration, random );
                tx.success();
            }
        }
        finally
        {
            tx.finish();
        }

    }

    private static boolean iterationIsInRange( int startIteration, BatchCommand command, int iteration )
    {
        return iteration < (startIteration + command.batchSize()) && iteration < command.numberOfIterations();
    }

    private static String elapsedTime( long startTime )
    {
        String ms = String.valueOf( (System.nanoTime() - startTime) / 1000000 );
        return String.format( "(elapsed: %s ms)", ms );
    }
}
