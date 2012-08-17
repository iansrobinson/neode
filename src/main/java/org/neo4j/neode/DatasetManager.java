package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.logging.Log;
import org.neo4j.neode.properties.Property;

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

    public NodeSpecification newNodeSpecification(String label, Property... properties)
    {
        return new NodeSpecification( label, properties );
    }

    public Dataset newDataset( String description )
    {
        return new Dataset( description, db, log, random );
    }
}