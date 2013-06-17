package org.neo4j.neode.logging;

public final class SysOutLog implements Log
{
    public static final Log INSTANCE = new SysOutLog();

    private SysOutLog()
    {
    }

    @Override
    public void write( String value )
    {
        System.out.println( value );
    }
}
