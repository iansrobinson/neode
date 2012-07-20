package org.neo4j.datasetbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniqueRandNumberGenerator implements RandomNumberGenerator
{
    private final Random random;

    public UniqueRandNumberGenerator( Random random )
    {
        this.random = random;
    }

    @Override
    public List<Integer> generate( int minNumberOfResults, int maxNumberOfResults, int min, int max )
    {
        if ( maxNumberOfResults < minNumberOfResults )
        {
            throw new IllegalArgumentException(
                    "maxNumberOfResults must be greater than or equal to minNumberOfResults)" );
        }

        if ( max < min )
        {
            throw new IllegalArgumentException(
                    "max must be greater than or equal to min" );
        }

        if ( minNumberOfResults < 0 )
        {
            throw new IllegalArgumentException( "minNumberOfResults must be greater than or equal to 0" );
        }

        if ( min < 0 )
        {
            throw new IllegalArgumentException( "min must be greater than or equal to 0" );
        }

        if ( (max - min == 0) && (maxNumberOfResults > 1) )
        {
            throw new IllegalArgumentException(
                    "When (max - min == 0), maxNumberOfResults must be 0 or 1" );
        }

        if ( (max - min) < (maxNumberOfResults - minNumberOfResults) )
        {
            throw new IllegalArgumentException(
                    "(maxNumberOfResults - minNumberOfResults) must be greater than (max - min)" );
        }

        int numberOfResults = (maxNumberOfResults == minNumberOfResults) ? maxNumberOfResults :
                minNumberOfResults + random.nextInt( maxNumberOfResults - minNumberOfResults );
        List<Integer> generatedNumbers = new ArrayList<Integer>( numberOfResults );
        int i = 0;
        int upTo = max - min + 1;
        while ( i < numberOfResults )
        {
            int nextNumber = min + random.nextInt( upTo );
            System.out.println( nextNumber );
            if ( !generatedNumbers.contains( nextNumber ) )
            {
                generatedNumbers.add( nextNumber );
                i++;
            }
        }
        return generatedNumbers;
    }

    @Override
    public List<Integer> generate( int numberOfResults, int min, int max )
    {
        return generate( numberOfResults, numberOfResults, min, max );
    }

    @Override
    public int generateSingle( int min, int max )
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

        int upTo = max - min + 1;
        return min + random.nextInt( upTo );
    }

}
