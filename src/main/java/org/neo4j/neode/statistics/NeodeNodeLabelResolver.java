package org.neo4j.neode.statistics;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.IteratorUtil;

class NeodeNodeLabelResolver implements NodeLabelResolver
{
    @Override
    public String labelFor( Node node )
    {
        Label label = IteratorUtil.firstOrNull(node.getLabels());
        if (label == null) return null;
        return label.name();
    }
}
