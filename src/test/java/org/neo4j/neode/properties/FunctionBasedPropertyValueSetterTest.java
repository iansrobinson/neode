package org.neo4j.neode.properties;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.test.Db;

public class FunctionBasedPropertyValueSetterTest
{
    @Test
    public void shouldSetPropertyValueUsingSuppliedFunction() throws Exception
    {
        // given
        PropertyValueGenerator<Long> f = new PropertyValueGenerator<Long>()
        {

            @Override
            public Long generate( PropertyContainer propertyContainer, String propertyName, String nodeLabel,
                                  int iteration, Random random )
            {
                return 1L;
            }
        };

        FunctionBasedPropertyValueSetter<Long> setter = new FunctionBasedPropertyValueSetter<Long>( f );

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();


        // when
        setter.setProperty( node, "date", "project", 1, new Random() );
        tx.success();
        tx.finish();

        // then
        assertEquals( 1L, node.getProperty( "date" ) );
    }

    @Test
    public void shouldReturnGeneratedValue() throws Exception
    {
        // given
        PropertyValueGenerator<Long> f = new PropertyValueGenerator<Long>()
        {

            @Override
            public Long generate( PropertyContainer propertyContainer, String propertyName, String nodeLabel,
                                  int iteration, Random random )
            {
                return 1L;
            }
        };


        FunctionBasedPropertyValueSetter<Long> setter = new FunctionBasedPropertyValueSetter<Long>( f );

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();


        // when
        Object value = setter.setProperty( node, "date", "project", 1, new Random() );
        tx.success();
        tx.finish();

        // then
        assertEquals( 1L, value );
    }

    @Test
    public void shouldAllowForSettingPropertyBasedOnAnotherPropertyValue() throws Exception
    {
        // given
        PropertyValueGenerator<Long> f1 = new PropertyValueGenerator<Long>()
        {

            @Override
            public Long generate( PropertyContainer propertyContainer, String propertyName, String nodeLabel,
                                  int iteration, Random random )
            {
                return 1L;
            }
        };


        PropertyValueGenerator<Long> f2 = new PropertyValueGenerator<Long>()
        {
            @Override
            public Long generate( PropertyContainer propertyContainer, String propertyName, String nodeLabel,
                                  int iteration, Random random )
            {
                return Long.parseLong( propertyContainer.getProperty( "start-date" ).toString() ) + 1L;
            }
        };

        FunctionBasedPropertyValueSetter<Long> setter1 = new FunctionBasedPropertyValueSetter<Long>( f1 );
        FunctionBasedPropertyValueSetter<Long> setter2 = new FunctionBasedPropertyValueSetter<Long>( f2 );

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();


        // when
        setter1.setProperty( node, "start-date", "project", 1, new Random() );
        setter2.setProperty( node, "end-date", "project", 1, new Random() );
        tx.success();
        tx.finish();

        // then
        assertEquals( 2L, node.getProperty( "end-date" ) );
    }
}
