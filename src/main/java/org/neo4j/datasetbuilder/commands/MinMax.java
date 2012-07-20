package org.neo4j.datasetbuilder.commands;

public class MinMax
{
    public static MinMax minMax(int min, int max)
    {
        return new MinMax( min, max );
    }

    public static MinMax exactly(int count)
    {
        return new MinMax( count, count );
    }

    private final int min;
    private final int max;

    public MinMax( int min, int max )
    {
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
