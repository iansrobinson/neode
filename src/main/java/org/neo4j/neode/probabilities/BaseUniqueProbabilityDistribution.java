package org.neo4j.neode.probabilities;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.neode.Range;

import static org.neo4j.neode.Range.exactly;

abstract class BaseUniqueProbabilityDistribution extends ProbabilityDistribution
{
    @Override
    public final List<Integer> generateList( Range sizeRange, Range range )
    {
        if ( (range.difference() + 1) < sizeRange.max() )
        {
            throw new IllegalArgumentException(
                    String.format( "(range.difference() + 1) must be greater or equal to numberOfResultsRange.max() " +
                            "[numberOfResultsRange: %s, range: %s]", sizeRange, range ) );
        }

        int numberOfResults = (sizeRange.difference() == 0) ?
                sizeRange.max() :
                sizeRange.min() + random().nextInt( sizeRange.difference() );

        List<Integer> generatedNumbers = new ArrayList<Integer>( numberOfResults );

        while ( generatedNumbers.size() < numberOfResults )
        {
            int nextNumber = getNextNumber( range );
            if ( range.isInRange( nextNumber ) && !generatedNumbers.contains( nextNumber ) )
            {
                generatedNumbers.add( nextNumber );
            }
        }
        return generatedNumbers;
    }

    @Override
    public final List<Integer> generateList( int size, Range range )
    {
        return generateList( exactly( size ), range );
    }

    @Override
    public final int generateSingle( Range range )
    {
        int nextNumber = getNextNumber( range );
        while ( !range.isInRange( nextNumber ) )
        {
            nextNumber = getNextNumber( range );
        }

        return nextNumber;
    }

    protected abstract int getNextNumber( Range minMax );
}
