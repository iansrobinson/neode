package org.neo4j.neode.probabilities;

import java.util.List;

import org.junit.Test;

import org.neo4j.neode.Range;

import static org.junit.Assert.assertEquals;

import static org.neo4j.neode.Range.minMax;

public class BaseUniqueProbabilityDistributionTest
{
    @Test
    public void shouldThrowExceptionIfPossibleRangeSmallerThanMaxNumberOfResults() throws Exception
    {
        // given
        ProbabilityDistribution probabilityDistribution = new DummyProbabilityDistribution();

        try
        {
            // when
            probabilityDistribution.generateList( minMax( 0, 2 ), minMax( 1, 1 ) );
        }
        catch ( IllegalArgumentException e )
        {
            // then
            assertEquals( "(range.difference() + 1) must be greater or equal to numberOfResultsRange.max() " +
                    "[numberOfResultsRange: Range{min=0, max=2}, range: Range{min=1, max=1}]", e.getMessage() );
        }

    }

    @Test
    public void shouldAllowForZeroNumbersToBeGenerated() throws Exception
    {
        // given
        ProbabilityDistribution probabilityDistribution = new DummyProbabilityDistribution();

        // when
        List<Integer> results = probabilityDistribution.generateList( minMax( 0, 0 ), minMax( 0, 1 ) );

        // then
        assertEquals( 0, results.size() );
    }

    private class DummyProbabilityDistribution extends BaseUniqueProbabilityDistribution
    {
        @Override
        protected int getNextNumber( Range minMax )
        {
            return 0;
        }

        @Override
        public String description()
        {
            return "dummy";
        }
    }
}
