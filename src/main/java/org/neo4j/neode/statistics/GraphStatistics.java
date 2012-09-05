package org.neo4j.neode.statistics;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Node;

public class GraphStatistics
{
    private final Map<String, NodeStatistic> nodeStatistics;

    public GraphStatistics()
    {
        nodeStatistics = new HashMap<String, NodeStatistic>();
    }

    public void add( Node node )
    {
        String label = node.getProperty( "_label" ).toString();
        if (!nodeStatistics.containsKey( label ))
        {
            nodeStatistics.put( label, new NodeStatistic( label ) );
        }
        nodeStatistics.get( label ).update( node );
    }

    public Iterable<NodeStatistic> nodeStatistics()
    {
        return nodeStatistics.values();
    }
}
