package org.neo4j.neode.test;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.test.ImpermanentGraphDatabase;

public final class Db
{
    private Db()
    {
    }

    public static GraphDatabaseService impermanentDb()
    {
        return new ImpermanentGraphDatabase();
    }

    public static GraphDatabaseService tempDb()
    {
        return new EmbeddedGraphDatabase( createTempDatabaseDir().getAbsolutePath() );
    }

    private static File createTempDatabaseDir()
    {

        File d;
        try
        {
            d = File.createTempFile( "datasetbuilder-", "dir" );
            System.out.println( String.format( "Created a new Neo4j database at [%s]", d.getAbsolutePath() ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
        if ( !d.delete() )
        {
            throw new RuntimeException( "temp config directory pre-delete failed" );
        }
        if ( !d.mkdirs() )
        {
            throw new RuntimeException( "temp config directory not created" );
        }
        d.deleteOnExit();
        return d;
    }
}
