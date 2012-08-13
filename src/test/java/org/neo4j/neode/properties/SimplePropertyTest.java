package org.neo4j.neode.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Random;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.test.Db;

public class SimplePropertyTest
{
    @Test
    public void shouldSetPropertyValue() throws Exception
    {
        // given
        PropertyValueSetter propertyValueSetter = new PropertyValueSetter()
        {
            @Override
            public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration, Random
                    random )
            {
                return "value";
            }
        };
        Property property = new SimpleProperty( "name", propertyValueSetter, false );

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();

        // when
        property.setProperty( node, db, "user", 1, new Random() );
        tx.success();
        tx.finish();

        // then
        assertEquals( "value", node.getProperty( "name" ) );
        assertNull( db.index().forNodes( "user" ).get( "name", "value" ).getSingle() );
    }

    @Test
    public void shouldIndexIndexableProperty() throws Exception
    {
        // given
        PropertyValueSetter propertyValueSetter = new PropertyValueSetter()
        {
            @Override
            public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration, Random
                    random )
            {
                return "value";
            }
        };
        Property property = new SimpleProperty( "name", propertyValueSetter, true );

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();

        // when
        property.setProperty( node, db, "user", 1, new Random() );
        tx.success();
        tx.finish();

        // then
        assertEquals( node, db.index().forNodes( "user" ).get( "name", "value" ).getSingle() );
    }
}
