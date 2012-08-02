package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;

class IdBasedStringPropertyValueSetter extends PropertyValueSetter
{
    @Override
    public Object setProperty( PropertyContainer propertyContainer, String propertyName, String nodeLabel,
                               int index, Random random )
    {
        long id = propertyContainer instanceof Node ?
                ((Node) propertyContainer).getId() :
                ((Relationship) propertyContainer).getId();

        String value = String.format( "%s-%s", nodeLabel, id );
        propertyContainer.setProperty( propertyName, value );
        return value;
    }
}
