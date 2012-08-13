package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.index.Index;

class SimpleProperty extends Property
{
    private final String propertyName;
    private final PropertyValueSetter propertyValueSetter;
    private final boolean isIndexable;
    private Index<Node> nodeIndex;

    SimpleProperty( String propertyName, PropertyValueSetter propertyValueSetter, boolean isIndexable )
    {
        this.propertyName = propertyName;
        this.propertyValueSetter = propertyValueSetter;
        this.isIndexable = isIndexable;
    }

    @Override
    public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String nodeLabel,
                             int iteration, Random random )
    {
        Object value = propertyValueSetter.generateValue( propertyContainer, nodeLabel, iteration,
                random );
        propertyContainer.setProperty( propertyName, value );
        if ( isIndexable && propertyContainer instanceof Node )
        {
            if ( nodeIndex == null )
            {
                nodeIndex = db.index().forNodes( nodeLabel );
            }
            nodeIndex.add( (Node) propertyContainer, propertyName, value );
        }
    }
}
