package org.neo4j.neode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.test.Db;

public class CreateUniqueNodesTest
{
    @Test
    public void shouldCreateNewNodes() throws Exception
    {
        // given
        Random random = new Random(  );
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();

        CreateUniqueNodes command = new CreateUniqueNodes( new NodeSpecification( "user", Collections.<Property>emptyList(), db, random) );

        // when
        Iterable<Node> nodes = command.getNodes( 3, db, null, random );

        // then
        Iterator<Node> iterator = nodes.iterator();

        assertEquals("user", iterator.next().getProperty( "_label" ));
        assertEquals("user", iterator.next().getProperty( "_label" ));
        assertEquals("user", iterator.next().getProperty( "_label" ));
        assertFalse( iterator.hasNext() );

        tx.success();
        tx.finish();
    }
}