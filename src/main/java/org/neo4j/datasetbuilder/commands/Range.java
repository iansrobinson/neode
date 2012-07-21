package org.neo4j.datasetbuilder.commands;

public class Range
{
    public static Range minMax(int min, int max)
    {
        return new Range( min, max );
    }

    public static Range exactly(int count)
    {
        return new Range( count, count );
    }

    private final int min;
    private final int max;

    public Range( int min, int max )
    {
        if (min < 0)
        {
            throw new IllegalArgumentException( "Min must be greater or equal to 0" );
        }

        if (max < min)
        {
            throw new IllegalArgumentException( "Max must be greater or equal to min" );
        }

        this.min = min;
        this.max = max;
    }

    public int min()
    {
        return min;
    }

    public int max()
    {
        return max;
    }
}
