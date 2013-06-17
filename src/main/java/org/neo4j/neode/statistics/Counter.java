package org.neo4j.neode.statistics;

public class Counter
{
    private int count;
    private int total;
    private int high;
    private int low;

    Counter()
    {
        count = 0;
        total = 0;
        high = 0;
        low = 0;
    }

    void update( int value )
    {
        count++;
        total += value;
        if ( value > high )
        {
            high = value;
        }
        if ( low == 0 || value < low )
        {
            low = value;
        }
    }

    public int total()
    {
        return total;
    }

    public int high()
    {
        return high;
    }

    public int low()
    {
        return low;
    }

    public int average()
    {
        return total / count;
    }
}
