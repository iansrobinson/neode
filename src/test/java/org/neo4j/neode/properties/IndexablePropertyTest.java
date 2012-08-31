package org.neo4j.neode.properties;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.test.Db;

public class IndexablePropertyTest
{
    @Test
    public void shouldIndexProperty() throws Exception
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
        Property property = new IndexableProperty( "name", generator, "indexname" );

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();

        // when
        property.setProperty( node, db, "user", 1 );
        tx.success();
        tx.finish();

        // then
        assertEquals( node, db.index().forNodes( "indexname" ).get( "name", "value" ).getSingle() );
    }

    @Test
    public void shouldIndexPropertyInIndexNamedAfterLabelIfIndexNameNotSupplied() throws Exception
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
        Property property = new IndexableProperty( "name", generator, null );

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();

        // when
        property.setProperty( node, db, "user", 1 );
        tx.success();
        tx.finish();

        // then
        assertEquals( node, db.index().forNodes( "user" ).get( "name", "value" ).getSingle() );
    }
}
