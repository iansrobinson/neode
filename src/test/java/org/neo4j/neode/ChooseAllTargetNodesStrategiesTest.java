package org.neo4j.neode;

import java.util.Iterator;

import org.junit.Test;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.logging.Log;
import org.neo4j.neode.test.Db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.ChoiceOfTargetNodesStrategy.all;
import static org.neo4j.neode.Range.exactly;
import static org.neo4j.neode.TargetNodesStrategy.create;

public class ChooseAllTargetNodesStrategiesTest
{
    @Test
    public void shouldExecuteAllCommandsForEachNode() throws Exception
    {
        GraphDatabaseService db = Db.impermanentDb();
        Log log = new Log()
        {
            @Override
            public void write( String value )
            {
                // Do nothing
            }
        };
        DatasetManager dsm = new DatasetManager( db, log );
        Dataset dataset = dsm.newDataset( "Execute All Test" );

        NodeSpecification user = dsm.nodeSpecification( "user" );
        NodeSpecification workAddress = dsm.nodeSpecification( "work address" );
        NodeSpecification homeAddress = dsm.nodeSpecification( "home address" );
        RelationshipSpecification work_address = dsm.relationshipSpecification( "WORK_ADDRESS" );
        RelationshipSpecification home_address = dsm.relationshipSpecification( "HOME_ADDRESS" );

        NodeCollection users = user.create( 2 ).update( dataset );
        users.createRelationshipsTo(
                all(
                        create( workAddress )
                                .numberOfTargetNodes( 2 )
                                .relationship( work_address )
                                .relationshipConstraints( exactly( 1 ) ),
                        create( homeAddress )
                                .numberOfTargetNodes( 1 )
                                .relationship( home_address )
                                .relationshipConstraints( exactly( 1 ) ) ) )
                .update( dataset );

        dataset.end();

        Node user1Node = users.getNodeByPosition( 0 );

        Iterator<Relationship> workAddressRels1 = user1Node.getRelationships( withName( "WORK_ADDRESS" ) ).iterator();
        Iterator<Relationship> homeAddressRels1 = user1Node.getRelationships( withName( "HOME_ADDRESS" ) ).iterator();

        assertNotNull( workAddressRels1.next() );
        assertNotNull( workAddressRels1.next() );
        assertFalse( workAddressRels1.hasNext() );

        assertNotNull( homeAddressRels1.next() );
        assertFalse( homeAddressRels1.hasNext() );

        Node user2Node = users.getNodeByPosition( 1 );

        Iterator<Relationship> workAddressRels2 = user2Node.getRelationships( withName( "WORK_ADDRESS" ) ).iterator();
        Iterator<Relationship> homeAddressRels2 = user2Node.getRelationships( withName( "HOME_ADDRESS" ) ).iterator();

        assertNotNull( workAddressRels2.next() );
        assertNotNull( workAddressRels2.next() );
        assertFalse( workAddressRels2.hasNext() );

        assertNotNull( homeAddressRels2.next() );
        assertFalse( homeAddressRels2.hasNext() );

        db.shutdown();
    }
}
