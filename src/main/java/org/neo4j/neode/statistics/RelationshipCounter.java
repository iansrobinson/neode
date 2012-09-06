package org.neo4j.neode.statistics;

class RelationshipCounter
{
    private int incoming;
    private int outgoing;

    RelationshipCounter()
    {
        incoming = 0;
        outgoing = 0;
    }

    void incrementIncoming()
    {
        incoming++;
    }

    void incrementOutgoing()
    {
        outgoing++;
    }

    int incoming()
    {
        return incoming;
    }

    int outgoing()
    {
        return outgoing;
    }
}
