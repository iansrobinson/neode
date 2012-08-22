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
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.test.Db;

public class NodeCollectionNewTest
{
    @Test
    public void shouldReturnLabel() throws Exception
    {
        // given
        NodeCollectionNew nodeCollection = new NodeCollectionNew( Db.impermanentDb(), "user",
                Collections.<Long>emptyList()
        );

        // then
        assertEquals( "user", nodeCollection.label() );

    }

    @Test
    public void shouldReturnNumberOfNodes() throws Exception
    {
        Db.usingSampleDataset( new Db.WithSampleDataset()
        {
            @Override
            public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode )
            {
                // given
                NodeCollectionNew nodeCollection = new NodeCollectionNew( db, "user",
                        asList( firstNode.getId(), secondNode.getId(), thirdNode.getId() ) );

                // then
                assertEquals( 3, nodeCollection.size() );
            }
        } );
    }

    @Test
    public void shouldReturnNodeByPosition() throws Exception
    {
        Db.usingSampleDataset( new Db.WithSampleDataset()
        {
            @Override
            public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode )
            {
                // given
                NodeCollectionNew nodeCollection = new NodeCollectionNew( db, "user",
                        asList( firstNode.getId(), secondNode.getId(), thirdNode.getId() ) );

                // then
                assertEquals( firstNode, nodeCollection.getNode( 0 ) );
                assertEquals( secondNode, nodeCollection.getNode( 1 ) );
                assertEquals( thirdNode, nodeCollection.getNode( 2 ) );
            }
        } );
    }

    @Test
    public void shouldReturnNodeIdByPosition() throws Exception
    {
        Db.usingSampleDataset( new Db.WithSampleDataset()
        {
            @Override
            public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode )
            {
                // given
                NodeCollectionNew nodeCollection = new NodeCollectionNew( db, "user",
                        asList( firstNode.getId(), secondNode.getId(), thirdNode.getId() ) );

                // then
                assertEquals( firstNode.getId(), nodeCollection.getNodeId( 0 ) );
                assertEquals( secondNode.getId(), nodeCollection.getNodeId( 1 ) );
                assertEquals( thirdNode.getId(), nodeCollection.getNodeId( 2 ) );
            }
        } );
    }

    @Test
    public void shouldBeAbleToIterateNodes() throws Exception
    {
        Db.usingSampleDataset( new Db.WithSampleDataset()
        {
            @Override
            public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode )
            {
                // given
                NodeCollectionNew nodeCollection = new NodeCollectionNew( db, "user",
                        asList( firstNode.getId(), secondNode.getId(), thirdNode.getId() ) );

                // then
                Iterable<Node> expectedNodes = asList( firstNode, secondNode, thirdNode );
                assertThat( nodeCollection, returnsSameItems( expectedNodes ) );
            }
        } );
    }

    @Test
    public void shouldReturnSubsetOfSelf() throws Exception
    {
        Db.usingSampleDataset( new Db.WithSampleDataset()
        {
            @Override
            public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode )
            {
                // given
                NodeCollectionNew nodeCollection = new NodeCollectionNew( db, "user",
                        asList( firstNode.getId(), secondNode.getId(), thirdNode.getId() ) );

                // when
                NodeCollectionNew subset = nodeCollection.subset( asList( 0, 2 ) );

                // then
                Iterable<Node> expectedNodes = asList( firstNode, thirdNode );
                assertThat( subset, returnsSameItems( expectedNodes ) );
            }
        } );
    }

    @Test
    public void shouldBeAbleToCombineCollections() throws Exception
    {
        Db.usingSampleDataset( new Db.WithSampleDataset()
        {
            @Override
            public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode )
            {
                // given
                NodeCollectionNew firstNodeCollection = new NodeCollectionNew( db, "user",
                        asList( firstNode.getId(), secondNode.getId() ) );

                NodeCollectionNew secondNodeCollection = new NodeCollectionNew( db, "user",
                        asList( thirdNode.getId() ) );

                // when
                NodeCollectionNew combined = firstNodeCollection.combine( secondNodeCollection );

                // then
                Iterable<Node> expectedNodes = asList( firstNode, secondNode, thirdNode );
                assertThat( combined, returnsSameItems( expectedNodes ) );
            }
        } );
    }

    @Test
    public void shouldBeAbleToAddNode() throws Exception
    {
        Db.usingSampleDataset( new Db.WithSampleDataset()
        {
            @Override
            public void execute( GraphDatabaseService db, Node firstNode, Node secondNode, Node thirdNode )
            {
                // given
                NodeCollectionNew nodeCollection = new NodeCollectionNew( db, "user", 3 );

                // when
                nodeCollection.add(firstNode);
                nodeCollection.add(secondNode);
                nodeCollection.add(thirdNode);

                // then
                Iterable<Node> expectedNodes = asList( firstNode, secondNode, thirdNode );
                assertThat( nodeCollection, returnsSameItems( expectedNodes ) );
            }
        } );
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
                    result &= expectedIterator.next().equals( iterator.next() );
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
