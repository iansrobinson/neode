package org.neo4j.neode.statistics;

public class RelationshipStatistic
{
    private final String label;
    private Counter incomingCount;
    private Counter outgoingCount;

    public RelationshipStatistic( String label )
    {
        this.label = label;
        incomingCount = new Counter();
        outgoingCount = new Counter();
    }

    public String label()
    {
        return label;
    }

    public void update( RelationshipCounter counter )
    {
        incomingCount.update( counter.incoming() );
        outgoingCount.update( counter.outgoing() );
    }

    public Counter incoming()
    {
        return incomingCount;
    }

    public Counter outgoing()
    {
        return outgoingCount;
    }
}
