package org.neo4j.neode.statistics;

public class RelationshipCounter
{
    private int incoming;
    private int outgoing;

    public RelationshipCounter()
    {
        incoming = 0;
        outgoing = 0;
    }

    public void incrementIncoming()
    {
        incoming++;
    }

    public void incrementOutgoing()
    {
        outgoing++;
    }

    public int incoming()
    {
        return incoming;
    }

    public int outgoing()
    {
        return outgoing;
    }
}
