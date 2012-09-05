package org.neo4j.neode.statistics;

public class RelationshipStatistic
{
    private final String label;
    private int nodeCount;
    private int incomingCount;
    private int outgoingCount;

    public RelationshipStatistic( String label )
    {
        this.label = label;
        nodeCount = 0;
        incomingCount = 0;
        outgoingCount = 0;
    }

    public String label()
    {
        return label;
    }

    public void update(RelationshipCounter counter)
    {
        nodeCount++;
        incomingCount+=counter.incoming();
        outgoingCount+=counter.outgoing();
    }

    public int averageIncoming()
    {
        return incomingCount/nodeCount;
    }

    public int averageOutgoing()
    {
        return outgoingCount/nodeCount;
    }
}
