package org.neo4j.neode.statistics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NodeStatistic
{
    private final String label;
    private int count;
    private Map<String, RelationshipStatistic> relationshipStatistics;

    public NodeStatistic( String label )
    {
        this.label = label;
        count = 0;
        relationshipStatistics = new HashMap<String, RelationshipStatistic>(  );
    }

    public String label()
    {
         return label;
    }

    public void update(Node node)
    {
        count++;

        Map<String, RelationshipCounter> relationshipCounters = new HashMap<String, RelationshipCounter>(  );

        for ( Relationship relationship : node.getRelationships() )
        {
            String label = relationship.getType().name();

            RelationshipCounter relationshipCounter =
                    relationshipCounters.containsKey( label ) ?
                    relationshipCounters.get( label ) :
                    relationshipCounters.put( label, new RelationshipCounter() );

            if (relationship.getStartNode().equals( node ))
            {
                relationshipCounter.incrementOutgoing();
            }
            else
            {
                relationshipCounter.incrementIncoming();
            }
        }

//        for ( String label : relationshipCounters.keySet() )
//        {
//            RelationshipStatistic re
//        }
    }

    public int count()
    {
        return count;
    }

    public Iterator<RelationshipStatistic> relationshipStatistics()
    {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
