package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class NodeCollectionTest
{
    @Test
    public void shouldReturnIdAtSpecifiedIndex() throws Exception
    {
        // given
        NodeCollection nodeCollection = new NodeCollection( "user", asList(1L, 2L, 3L) );

        // then
        assertEquals( (Object) 2L, nodeCollection.getId( 1 ));
    }
}
