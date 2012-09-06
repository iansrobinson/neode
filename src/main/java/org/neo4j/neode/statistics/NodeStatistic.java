package org.neo4j.neode.statistics;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NodeStatistic
{
    private final String label;
    private int count;
    private Map<String, RelationshipStatistic> relationshipStatistics;

    NodeStatistic( String label )
    {
        this.label = label;
        count = 0;
        relationshipStatistics = new HashMap<String, RelationshipStatistic>();
    }

    public String label()
    {
        return label;
    }

    void update( Node node )
    {
        count++;

        Map<String, RelationshipCounter> relationshipCounters = new HashMap<String, RelationshipCounter>();

        for ( Relationship relationship : node.getRelationships() )
        {
            String label = relationship.getType().name();

            RelationshipCounter relationshipCounter =  relationshipCounters.get( label );
            if (relationshipCounter == null)
            {
                relationshipCounters.put( label, relationshipCounter = new RelationshipCounter() );
            }

            if ( relationship.getStartNode().equals( node ) )
            {
                relationshipCounter.incrementOutgoing();
            }
            else
            {
                relationshipCounter.incrementIncoming();
            }
        }

        for ( String label : relationshipCounters.keySet() )
        {
            RelationshipStatistic relationshipStatistic =  relationshipStatistics.get( label );
            if (relationshipStatistic == null)
            {
                relationshipStatistics.put( label, relationshipStatistic = new RelationshipStatistic( label ) );
            }

            relationshipStatistic.update( relationshipCounters.get( label ) );
        }
    }

    public int count()
    {
        return count;
    }

    public Iterable<RelationshipStatistic> relationshipStatistics()
    {
        return relationshipStatistics.values();
    }

    public RelationshipStatistic getRelationshipStatistic( String label )
    {
        return relationshipStatistics.get( label );
    }

}
