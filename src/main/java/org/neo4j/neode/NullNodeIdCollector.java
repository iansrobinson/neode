package org.neo4j.neode;

import java.util.Collections;
import java.util.List;

class NullNodeIdCollector implements NodeIdCollector
{
    public static NodeIdCollector INSTANCE = new NullNodeIdCollector();

    private NullNodeIdCollector(){}

    @Override
    public void add( Long nodeId )
    {
        // Do nothing
    }

    @Override
    public List<Long> nodeIds()
    {
        return Collections.emptyList();
    }
}
