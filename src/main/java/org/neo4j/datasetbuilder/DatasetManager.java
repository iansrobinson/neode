package org.neo4j.datasetbuilder;

import java.util.Random;

import org.neo4j.datasetbuilder.logging.Log;
import org.neo4j.graphdb.GraphDatabaseService;

public class DatasetManager
{
    private final GraphDatabaseService db;
    private final Log log;
    private final Random random;

    public DatasetManager( GraphDatabaseService db, Log log )
    {
        this.db = db;
        this.log = log;
        random = new Random();
    }

    public Dataset newDataset( String description )
    {
        return new Dataset( description, db, log, random );
    }
}