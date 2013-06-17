package org.neo4j.neode.statistics;

import org.neo4j.graphdb.Node;

class NeodeNodeLabelResolver implements NodeLabelResolver
{
    @Override
    public String labelFor( Node node )
    {
        if ( node.hasProperty( "_label" ) )
        {
            return node.getProperty( "_label" ).toString();
        }

        return null;
    }
}
