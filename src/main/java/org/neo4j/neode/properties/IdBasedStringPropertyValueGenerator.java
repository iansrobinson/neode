package org.neo4j.neode.properties;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;

class IdBasedStringPropertyValueGenerator extends PropertyValueGenerator
{
    @Override
    public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration )
    {
        long id = propertyContainer instanceof Node ?
                ((Node) propertyContainer).getId() :
                ((Relationship) propertyContainer).getId();

        return String.format( "%s-%s", nodeLabel, id );
    }
}
