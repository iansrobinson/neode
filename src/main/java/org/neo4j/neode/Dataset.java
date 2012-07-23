package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.neode.commands.BatchCommand;
import org.neo4j.neode.logging.Log;

public class Dataset
{
    private final String description;
    private final GraphDatabaseService db;
    private final Log log;
    private final Random random;
    private final long runStartTime;

    public Dataset( String description, GraphDatabaseService db, Log log, Random random )
    {
        this.description = description;
        this.db = db;
        this.log = log;
        this.random = random;
        runStartTime = System.nanoTime();
        log.write( String.format( "Begin [%s]\n", description ) );
    }

    public DomainEntityInfo execute( BatchCommand command )
    {
        long startTime = System.nanoTime();

        log.write( String.format( "Begin [%s Iterations: %s, BatchSize: %s]",
                command.description(), command.numberOfIterations(), command.batchSize() ) );
        command.onBegin( log );
        for ( int index = 0; index < command.numberOfIterations(); index += command.batchSize() )
        {
            doExecute( index, command, startTime );
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

    private void doExecute( int startIndex, BatchCommand command, long startTime )
    {
        log.write( String.format( "      [Beginning " + startIndex + " of " + command.numberOfIterations() +
                ", '%s'] %s", command.shortDescription(), elapsedTime( startTime ) ) );

        Transaction tx = db.beginTx();
        try
        {
            for ( int index = startIndex; indexIsInRange( startIndex, command, index ); index++ )
            {
                command.execute( db, index, random );
                tx.success();
            }
        }
        finally
        {
            tx.finish();
        }

    }

    private static boolean indexIsInRange( int startIndex, BatchCommand command, int index )
    {
        return index < (startIndex + command.batchSize()) && index < command.numberOfIterations();
    }

    private static String elapsedTime( long startTime )
    {
        String ms = String.valueOf( (System.nanoTime() - startTime) / 1000000 );
        return String.format( "(elapsed: %s)", ms );
    }
}
