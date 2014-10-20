package org.neo4j.neode.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserters;

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
        Node firstNode, secondNode, thirdNode;
        try ( Transaction tx = db.beginTx() )
        {
            firstNode = db.createNode();
            secondNode = db.createNode();
            thirdNode = db.createNode();
            tx.success();
        }

        try ( Transaction tx = db.beginTx() )
        {
            f.execute( db, firstNode, secondNode, thirdNode );
            tx.success();
        }
        db.shutdown();
    }

    public interface WithSampleDataset
    {
        public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode );
    }


    public static GraphDatabaseService getBatchInserterDB(String location)
    {
        File file = new File(location);
        if(file.exists() && file.isDirectory())
        {
            try
            {
                FileUtils.deleteRecursively(file);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return BatchInserters.batchDatabase(location);
    }
}
