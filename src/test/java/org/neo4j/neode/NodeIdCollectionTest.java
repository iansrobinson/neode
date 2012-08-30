package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.Iterator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

public class NodeIdCollectionTest
{
    @Test
    public void shouldReturnLabel() throws Exception
    {
        // given
        NodeIdCollection nodeIdCollection = new NodeIdCollection( "user", Collections.<Long>emptyList() );

        // then
        assertEquals( "user", nodeIdCollection.label() );

    }

    @Test
    public void shouldReturnNumberOfNodes() throws Exception
    {
        // given
        NodeIdCollection nodeIdCollection = new NodeIdCollection( "user", asList( 1L, 2L, 3L ) );

        // then
        assertEquals( 3, nodeIdCollection.size() );
    }

    @Test
    public void shouldReturnNodeIdByPosition() throws Exception
    {
        // given
        NodeIdCollection nodeIdCollection = new NodeIdCollection( "user", asList( 1L, 2L, 3L ) );

        // then
        assertEquals( (Object) 1L, nodeIdCollection.getIdByPosition( 0 ) );
        assertEquals( (Object) 2L, nodeIdCollection.getIdByPosition( 1 ) );
        assertEquals( (Object) 3L, nodeIdCollection.getIdByPosition( 2 ) );

    }

    @Test
    public void shouldBeAbleToIterateNodeIds() throws Exception
    {
        // given
        NodeIdCollection nodeIdCollection = new NodeIdCollection( "user", asList( 1L, 2L, 3L ) );

        // then
        Iterable<Long> expectedIds = asList( 1L, 2L, 3L );
        assertThat( nodeIdCollection, returnsSameItems( expectedIds ) );
    }

    @Test
    public void shouldReturnSubsetOfSelf() throws Exception
    {
        // given
        NodeIdCollection nodeIdCollection = new NodeIdCollection( "user", asList( 1L, 2L, 3L ) );

        // when
        NodeIdCollection subset = nodeIdCollection.subset( asList( 0, 2 ) );

        // then
        Iterable<Long> expectedIds = asList( 1L, 3L );
        assertThat( subset, returnsSameItems( expectedIds ) );

    }

    @Test
    public void shouldBeAbleToCombineCollections() throws Exception
    {
        // given
        NodeIdCollection firstNodeIdCollection = new NodeIdCollection( "user", asList( 1L ) );
        NodeIdCollection secondNodeIdCollection = new NodeIdCollection( "user", asList( 2L, 3L ) );

        // when
        NodeIdCollection combined = firstNodeIdCollection.combine( secondNodeIdCollection );

        // then
        Iterable<Long> expectedNodeIds = asList( 1L, 2L, 3L );
        assertThat( combined, returnsSameItems( expectedNodeIds ) );
    }

    private <T> Matcher<Iterable<T>> returnsSameItems( final Iterable<T> expected )
    {
        return new TypeSafeMatcher<Iterable<T>>()
        {
            @Override
            public boolean matchesSafely( Iterable<T> actual )
            {
                Iterator<T> expectedIterator = expected.iterator();
                Iterator<T> iterator = actual.iterator();
                boolean result = true;
                while ( expectedIterator.hasNext() && iterator.hasNext() )
                {

                    T expectedNext = expectedIterator.next();
                    T next = iterator.next();
                    if ( expectedNext == null && next == null )
                    {
                        result &= true;
                    }
                    else
                    {
                        result &= expectedNext.equals( next );
                    }
                }
                return result &= (!expectedIterator.hasNext()) && (!iterator.hasNext());
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( "Iterables do not return the same items" );
            }
        };
    }

}
