package org.neo4j.neode.numbergenerators;

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

    private Range( int min, int max )
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

    public int difference()
    {
        return max - min;
    }

    public boolean isInRange( int value )
    {
        return value >= min() && value <= max;
    }

    @Override
    public String toString()
    {
        return "Range{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
