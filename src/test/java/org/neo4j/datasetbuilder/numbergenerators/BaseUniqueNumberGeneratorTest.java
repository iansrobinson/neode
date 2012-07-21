package org.neo4j.datasetbuilder.numbergenerators;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

import org.junit.Test;

public class BaseUniqueNumberGeneratorTest
{
    @Test
    public void shouldThrowExceptionIfMaxNumberOfResultsLessThanMinNumberOfResults() throws Exception
    {
        // given
        NumberGenerator numberGenerator = new DummyNumberGenerator();

        try
        {
            // when
            numberGenerator.generate( 1, 0, 1, 2, new Random() );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "maxNumberOfResults must be greater than or equal to minNumberOfResults " +
                    "[minNumberOfResults: 1, maxNumberOfResults: 0, min: 1, max: 2]", e.getMessage() );
        }

    }

    @Test
    public void shouldThrowExceptionIfMaxLessThanMin() throws Exception
    {
        // given
        NumberGenerator numberGenerator = new DummyNumberGenerator();

        try
        {
            // when
            numberGenerator.generate( 1, 2, 1, 0, new Random() );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "max must be greater than or equal to min " +
                    "[minNumberOfResults: 1, maxNumberOfResults: 2, min: 1, max: 0]", e.getMessage() );
        }

    }

    @Test
    public void shouldThrowExceptionIfMinNumberOfResultsLessThanZero() throws Exception
    {
        // given
        NumberGenerator numberGenerator = new DummyNumberGenerator();

        try
        {
            // when
            numberGenerator.generate( -1, 1, 0, 1, new Random() );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "minNumberOfResults must be greater than or equal to zero " +
                    "[minNumberOfResults: -1, maxNumberOfResults: 1, min: 0, max: 1]", e.getMessage() );
        }

    }

    @Test
    public void shouldThrowExceptionIfMinLessThanZero() throws Exception
    {
        // given
        NumberGenerator numberGenerator = new DummyNumberGenerator();

        try
        {
            // when
            numberGenerator.generate( 0, 1, -1, 1, new Random() );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "min must be greater than or equal to zero " +
                    "[minNumberOfResults: 0, maxNumberOfResults: 1, min: -1, max: 1]", e.getMessage() );
        }

    }

    @Test
    public void shouldThrowExceptionIfPossibleRangeSmallerThanMaxNumberOfResults() throws Exception
    {
        // given
        NumberGenerator numberGenerator = new DummyNumberGenerator();

        try
        {
            // when
            numberGenerator.generate( 0, 2, 1, 1, new Random() );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "(max - min + 1) must be greater or equal to maxNumberOfResults " +
                    "[minNumberOfResults: 0, maxNumberOfResults: 2, min: 1, max: 1]", e.getMessage() );
        }

    }

    @Test
    public void shouldAllowForZeroNumbersToBeGenerated() throws Exception
    {
        // given
        NumberGenerator numberGenerator = new DummyNumberGenerator();

        // when
        List<Integer> results = numberGenerator.generate( 0, 0, 0, 1, new Random() );

        // then
        assertEquals( 0, results.size() );
    }

    private class DummyNumberGenerator extends BaseUniqueNumberGenerator
    {
        @Override
        protected int getNextNumber( int min, int upTo, Random random )
        {
            return 0;
        }
    }
}
