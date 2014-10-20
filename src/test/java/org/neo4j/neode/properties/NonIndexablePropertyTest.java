package org.neo4j.neode.properties;

import org.junit.Test;

import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.neode.test.Db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NonIndexablePropertyTest
{
    @Test
    public void shouldSetPropertyValue() throws Exception
    {
        // given
        PropertyValueGenerator generator = new PropertyValueGenerator()
        {
            @Override
            public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration )
            {
                return "value";
            }
        };
        GraphDatabaseService db = Db.impermanentDb();

        Property property = new NonIndexableProperty( "name", generator );

        Node node;
        try ( Transaction tx = db.beginTx() ) {
            node = db.createNode();

            // when
            property.setProperty( node, "user", 1 );
            tx.success();
        }

        // then
        try ( Transaction tx = db.beginTx() ) {
            assertEquals( "value", node.getProperty( "name" ) );
            assertNull( IteratorUtil.singleOrNull(db.findNodesByLabelAndProperty(DynamicLabel.label("user"), "name", "value")) );
        }
    }
}
