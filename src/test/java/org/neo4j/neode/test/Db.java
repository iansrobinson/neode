package org.neo4j.neode.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.test.TestGraphDatabaseFactory;

public final class Db
{
    private Db()
    {
    }

    public static GraphDatabaseService impermanentDb()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put( "online_backup_enabled", "false" );

        return new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().setConfig( params ).newGraphDatabase();
    }

    public static GraphDatabaseService tempDb()
    {
        return new GraphDatabaseFactory().newEmbeddedDatabase( createTempDatabaseDir().getAbsolutePath() );
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
        db.shutdown();
    }

    public interface WithSampleDataset
    {
        public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode );
    }


}
