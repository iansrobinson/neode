package org.neo4j.neode.numbergenerators;

import static org.neo4j.neode.numbergenerators.Range.exactly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class BaseUniqueProbabilityDistribution extends ProbabilityDistribution
{
    @Override
    public final List<Integer> generateList( Range sizeRange, Range range,
                                             Random random )
    {
        if ( (range.difference() + 1) < sizeRange.max() )
        {
            throw new IllegalArgumentException(
                    String.format("(range.difference() + 1) must be greater or equal to numberOfResultsRange.max() " +
                            "[numberOfResultsRange: %s, range: %s]", sizeRange, range) );
        }

        int numberOfResults = (sizeRange.difference() == 0) ?
                sizeRange.max() :
                sizeRange.min() + random.nextInt( sizeRange.difference() );

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
    public final List<Integer> generateList( int size, Range range, Random random )
    {
        return generateList( exactly( size ), range, random );
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
