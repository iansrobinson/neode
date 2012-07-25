package org.neo4j.neode.properties;

import org.neo4j.graphdb.Node;

class NodeIdBasedStringPropertyValueSetter extends PropertyValueSetter
{
    @Override
    public Object setProperty( Node node, String propertyName, String entityName, int index )
    {
        String value = String.format( "%s-%s", entityName, node.getId() );
        node.setProperty( propertyName, value );
        return value;
    }
}
