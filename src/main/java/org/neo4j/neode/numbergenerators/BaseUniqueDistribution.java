package org.neo4j.neode.numbergenerators;

import static org.neo4j.neode.numbergenerators.Range.exactly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class BaseUniqueDistribution extends Distribution
{
    @Override
    public final List<Integer> generate( Range numberOfResultsRange, Range range,
                                         Random random )
    {
        if ( (range.difference() + 1) < numberOfResultsRange.max() )
        {
            throw new IllegalArgumentException(
                    String.format("(range.difference() + 1) must be greater or equal to numberOfResultsRange.max() " +
                            "[numberOfResultsRange: %s, range: %s]", numberOfResultsRange, range) );
        }

        int numberOfResults = (numberOfResultsRange.difference() == 0) ?
                numberOfResultsRange.max() :
                numberOfResultsRange.min() + random.nextInt( numberOfResultsRange.difference() );

        List<Integer> generatedNumbers = new ArrayList<Integer>( numberOfResults );

        while ( generatedNumbers.size() < numberOfResults )
        {
            int nextNumber = getNextNumber( range, random );
            if ( range.isInRange( nextNumber )  && !generatedNumbers.contains( nextNumber ) )
            {
                generatedNumbers.add( nextNumber );
            }
        }
        return generatedNumbers;
    }

    @Override
    public final List<Integer> generate( int numberOfResults, Range range, Random random )
    {
        return generate( exactly( numberOfResults ), range, random );
    }

    @Override
    public final int generateSingle( Range range, Random random )
    {
        int nextNumber = getNextNumber( range, random );
        while ( !range.isInRange( nextNumber ) )
        {
            nextNumber = getNextNumber( range, random );
        }

        return nextNumber;
    }

    protected abstract int getNextNumber( Range minMax, Random random );
}
