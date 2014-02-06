package org.neo4j.neode;

import java.util.Iterator;

import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.probabilities.ProbabilityDistribution;
import org.neo4j.neode.test.Db;

import static java.util.Arrays.asList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.neo4j.neode.properties.Property.property;
import static org.neo4j.neode.properties.PropertyValueGenerator.iterationBased;

public class GetOrCreateUniqueNodesTest
{
    @Test
    public void shouldReturnSpecifiedNumberOfNodes() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        try (Transaction tx = db.beginTx())
        {

            NodeSpecification nodeSpecification = new NodeSpecification( "user",
                    asList( property( "name", iterationBased() ) ), db );
            ProbabilityDistribution probabilityDistribution = mock( ProbabilityDistribution.class );
            when( probabilityDistribution.generateList( 2, Range.minMax( 0, 4 ) ) ).thenReturn( asList( 0, 4 ) );
            when( probabilityDistribution.generateList( 3, Range.minMax( 0, 4 ) ) ).thenReturn( asList( 1, 4, 2 ) );

            GetOrCreateUniqueNodes targetNodesSource = new GetOrCreateUniqueNodes( nodeSpecification, 5,
                    probabilityDistribution );

            // when
            targetNodesSource.getTargetNodes( 2, null );
            Iterable<Node> targetNodes = targetNodesSource.getTargetNodes( 3, null );

            // then
            Iterator<Node> iterator = targetNodes.iterator();
            Node firstNode = iterator.next();
            Node secondNode = iterator.next();
            Node thirdNode = iterator.next();
            assertFalse( iterator.hasNext() );


            assertEquals( "user-5", firstNode.getProperty( "name" ) );
            assertEquals( 1L, firstNode.getId() );

            assertEquals( "user-2", secondNode.getProperty( "name" ) );
            assertEquals( 2L, secondNode.getId() );


            assertEquals( "user-3", thirdNode.getProperty( "name" ) );
            assertEquals( 3L, thirdNode.getId() );
            tx.success();
        }
    }
}
