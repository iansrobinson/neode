package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public class SparseNodeListGeneratorTest
{
    @Test
    public void shouldReturnSparseListOfExistingNodes() throws Exception
    {
        // given
        Node currentNode = mock( Node.class );

        GraphQuery query = mock( GraphQuery.class );
        Node node0 = mock( Node.class );
        Node node1 = mock( Node.class );
        when( query.execute( currentNode ) ).thenReturn( asList( node0, node1 ) );

        ProbabilityDistribution probabilityDistribution = mock( ProbabilityDistribution.class );
        when( probabilityDistribution.generateList( 6, Range.minMax( 0, 5 ) ) ).thenReturn( asList( 0, 1, 2,
                3, 4, 5 ) );

        SparseNodeListGenerator generator = new SparseNodeListGenerator( query, 1.2, probabilityDistribution );

        // when
        List<Node> results = generator.getSparseListOfExistingNodes( 5, currentNode );

        // then
        assertEquals( 5, results.size() );
        assertEquals( node0, results.get( 0 ) );
        assertEquals( node1, results.get( 1 ) );
        assertNull( results.get( 2 ) );
        assertNull( results.get( 3 ) );
        assertNull( results.get( 4 ) );
    }

    @Test
    public void shouldReturnListOfNullsWhenQueryCannotFindNodes() throws Exception
    {
        // given
        Node currentNode = mock( Node.class );

        GraphQuery query = mock( GraphQuery.class );
        when( query.execute( currentNode ) ).thenReturn( Collections.<Node>emptyList() );

        ProbabilityDistribution probabilityDistribution = mock( ProbabilityDistribution.class );
        when( probabilityDistribution.generateList( 6, Range.minMax( 0, 5 ) ) ).thenReturn( asList( 0, 1, 2,
                3, 4, 5 ) );

        SparseNodeListGenerator generator = new SparseNodeListGenerator( query, 1.2, probabilityDistribution );

        // when
        List<Node> results = generator.getSparseListOfExistingNodes( 5, currentNode );

        // then
        for ( Node result : results )
        {
            assertNull( result );
        }
    }

    @Test
    public void shouldThrowExceptionWhenProportionOfNodesToListSizeIsLessThanOne() throws Exception
    {
        try
        {
            // when
            new SparseNodeListGenerator( mock( GraphQuery.class ), 0.9, mock( ProbabilityDistribution.class ) );
            fail( "Expected IllegalArgumentException" );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "proportionOfNodesToListSize must be greater than or equal to 1.0", e.getMessage() );
        }

    }
}
