package org.neo4j.neode.properties;

import org.junit.Test;

import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.neode.test.Db;

import static org.junit.Assert.assertEquals;

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
        GraphDatabaseService db = Db.impermanentDb();

        Property property = new IndexableProperty( db, "user", "name", generator, "indexname" );

        Node node;
        try ( Transaction tx = db.beginTx() )
        {
            node = db.createNode();

            // when
            property.setProperty( node, "user", 1 );
            tx.success();
        }

        // then
        try ( Transaction tx = db.beginTx() )
        {
            assertEquals( node, IteratorUtil.singleOrNull(db.findNodesByLabelAndProperty(DynamicLabel.label("indexname"), "name", "value" )) );
            assertEquals( node, IteratorUtil.singleOrNull(db.findNodesByLabelAndProperty(DynamicLabel.label("user"), "name", "value" )) );
            tx.success();
        }
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
        GraphDatabaseService db = Db.impermanentDb();

        Property property = new IndexableProperty( db, "user", "name", generator );

        Node node;
        try ( Transaction tx = db.beginTx() )
        {
            node = db.createNode();

            // when
            property.setProperty( node, "user", 1 );
            tx.success();
        }

        // then
        try ( Transaction tx = db.beginTx() )
        {
            assertEquals( node, IteratorUtil.singleOrNull(db.findNodesByLabelAndProperty(DynamicLabel.label("user"), "name", "value" )) );
            tx.success();
        }
    }
}
