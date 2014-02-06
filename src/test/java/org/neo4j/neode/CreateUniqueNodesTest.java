package org.neo4j.neode;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Test;

import org.neo4j.graphdb.*;
import org.neo4j.neode.properties.Property;
import org.neo4j.neode.test.Db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CreateUniqueNodesTest
{
    @Test
    public void shouldCreateNewNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Label user = DynamicLabel.label("user");
        try ( Transaction tx = db.beginTx() ) {

        CreateUniqueNodes command = new CreateUniqueNodes(
                new NodeSpecification( user, Collections.<Property>emptyList(), db ) );

        // when
        Iterable<Node> nodes = command.getTargetNodes( 3, null );

        // then
        Iterator<Node> iterator = nodes.iterator();

        assertEquals( true, iterator.next().hasLabel( user ) );
        assertEquals( true, iterator.next().hasLabel( user ) );
        assertEquals( true, iterator.next().hasLabel( user ) );
        assertFalse( iterator.hasNext() );

        tx.success();
        }
    }
}
