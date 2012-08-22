package org.neo4j.neode.test;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
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

    public static void usingSampleDataset( WithSampleDataset f )
    {
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        Node thirdNode = db.createNode();
        tx.success();
        tx.finish();

        f.execute( db, firstNode, secondNode, thirdNode );
    }

    public interface WithSampleDataset
    {
        public void execute(GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode);
    }


}
