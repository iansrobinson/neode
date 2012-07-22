package org.neo4j.datasetbuilder;

import java.util.Random;

import org.neo4j.datasetbuilder.logging.Log;
import org.neo4j.graphdb.GraphDatabaseService;

public class BatchCommandExecutor
{
    private final GraphDatabaseService db;
    private final Log log;
    private final Random random;

    public BatchCommandExecutor( GraphDatabaseService db, Log log )
    {
        this.db = db;
        this.log = log;
        random = new Random();
    }

    public Run newRun( String description )
    {
        return new Run( description, db, log, random );
    }
}