package org.neo4j.neode.numbergenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class BaseUniqueNumberGenerator implements NumberGenerator
{
    @Override
    public final List<Integer> generate( int minNumberOfResults, int maxNumberOfResults, int min, int max,
                                         Random random )
    {
        if ( maxNumberOfResults < minNumberOfResults )
        {
            throw new IllegalArgumentException(
                    formatErrorMessage( "maxNumberOfResults must be greater than or equal to minNumberOfResults",
                            minNumberOfResults, maxNumberOfResults, min, max ) );
        }

        if ( max < min )
        {
            throw new IllegalArgumentException(
                    formatErrorMessage( "max must be greater than or equal to min",
                            minNumberOfResults, maxNumberOfResults, min, max ) );
        }

        if ( minNumberOfResults < 0 )
        {
            throw new IllegalArgumentException(
                    formatErrorMessage( "minNumberOfResults must be greater than or equal to zero",
                            minNumberOfResults, maxNumberOfResults, min, max ) );
        }

        if ( min < 0 )
        {
            throw new IllegalArgumentException(
                    formatErrorMessage( "min must be greater than or equal to zero",
                            minNumberOfResults, maxNumberOfResults, min, max ) );
        }


        if ( (max - min + 1) < maxNumberOfResults )
        {
            throw new IllegalArgumentException(
                                formatErrorMessage( "(max - min + 1) must be greater or equal to maxNumberOfResults",
                                        minNumberOfResults, maxNumberOfResults, min, max ) );
        }

        int numberOfResults = (maxNumberOfResults == minNumberOfResults) ? maxNumberOfResults :
                minNumberOfResults + random.nextInt( maxNumberOfResults - minNumberOfResults );
        List<Integer> generatedNumbers = new ArrayList<Integer>( numberOfResults );
        int i = 0;
        int upTo = max - min;
        while ( i < numberOfResults )
        {
            int nextNumber = getNextNumber( min, upTo, random );
            if ( nextNumber >= min && nextNumber <= max && !generatedNumbers.contains( nextNumber ) )
            {
                generatedNumbers.add( nextNumber );
                i++;
            }
        }
        return generatedNumbers;
    }

    @Override
    public final List<Integer> generate( int numberOfResults, int min, int max, Random random )
    {
        return generate( numberOfResults, numberOfResults, min, max, random );
    }

    @Override
    public final int generateSingle( int min, int max, Random random )
    {
        if ( max < min )
        {
            throw new IllegalArgumentException(
                    "max must be greater than or equal to min" );
        }

        if ( min < 0 )
        {
            throw new IllegalArgumentException( "min must be greater than or equal to 0" );
        }

        int nextNumber = getNextNumber( min, max - min, random );
        while ( nextNumber < min || nextNumber > max )
        {
            nextNumber = getNextNumber( min, max - min, random );
        }

        return nextNumber;
    }

    protected abstract int getNextNumber( int min, int upTo, Random random );

    private String formatErrorMessage( String msg, int minNumberOfResults, int maxNumberOfResults, int min, int max )
    {
        return String.format( "%s [minNumberOfResults: %s, maxNumberOfResults: %s, min: %s, max: %s]",
                msg, minNumberOfResults, maxNumberOfResults, min, max );
    }

}
