package org.neo4j.neode.numbergenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.neo4j.neode.numbergenerators.Range.minMax;

import org.junit.Test;

public class RangeTest
{
    @Test
    public void shouldThrowExceptionIfMaxIsLessThan() throws Exception
    {
        try
        {
            // when
            Range.minMax( 1, 0 );
            fail( "Expected IllegalArgumentException" );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "Max must be greater or equal to min", e.getMessage() );
        }

    }

    @Test
    public void shouldThrowExceptionIfMinIsLessThanZero() throws Exception
    {
        try
        {
            // when
            Range.minMax( -1, 1 );
            fail( "Expected IllegalArgumentException" );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "Min must be greater or equal to 0", e.getMessage() );
        }

    }

    @Test
    public void shouldCalculateDifference() throws Exception
    {
        // given
        Range range = minMax( 1, 5 );

        // when
        int difference = range.difference();

        // then
        assertEquals( 4, difference );
    }

    @Test
    public void shouldDetermineWhetherSuppliedValueIsInRange() throws Exception
    {
        // given
        Range range = minMax( 1, 5 );

        // then
        assertTrue( range.isInRange(1) );
        assertTrue( range.isInRange(3) );
        assertTrue( range.isInRange(5) );
        assertFalse( range.isInRange( 0 ) );
        assertFalse( range.isInRange( 6 ) );
    }
}
