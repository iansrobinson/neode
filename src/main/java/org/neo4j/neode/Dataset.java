package org.neo4j.neode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.neode.logging.Log;

public class Dataset
{
    private final String description;
    private final GraphDatabaseService db;
    private final Log log;
    private final long runStartTime;

    Dataset( String description, GraphDatabaseService db, Log log )
    {
        this.description = description;
        this.db = db;
        this.log = log;
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
    //this should be updated since it is going to be deprecated
        //@deprecated This will be moved to internal packages in the next major release.
        if(db instanceof GraphDatabaseAPI)
        {
            log.write(String.format("Store [%s]\n\nEnd   [%s] %s\n",
                    ((GraphDatabaseAPI) db).getStoreDir(), description, elapsedTime(runStartTime)));
        }
        log.write( String.format( "End   [%s] %s\n", description, elapsedTime( runStartTime ) ) );
    }

    GraphDatabaseService db()
    {
        return db;
    }

    private void doExecute( int startIteration, BatchCommand command, long startTime )
    {
        log.write( String.format( "      [Beginning " + startIteration + " of " + command.numberOfIterations() +
                ", '%s'] %s", command.shortDescription(), elapsedTime( startTime ) ) );

        try (Transaction tx = db.beginTx()) {
            for (int iteration = startIteration; iterationIsInRange(startIteration, command,
                    iteration); iteration++) {
                command.execute(iteration);
                tx.success();
            }
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
