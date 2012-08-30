package org.neo4j.neode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.CreateRelationshipSpecification.create;
import static org.neo4j.neode.CreateRelationshipSpecificationChoices.all;
import static org.neo4j.neode.Range.exactly;

import java.util.Iterator;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.logging.SysOutLog;
import org.neo4j.neode.test.Db;

public class AllCreateRelationshipSpecificationChoicesTest
{
    @Test
    public void shouldExecuteAllCommandsForEachNode() throws Exception
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );
        Dataset dataset = dsm.newDataset( "Execute All Test" );

        NodeSpecification user = dsm.nodeSpecification( "user" );
        NodeSpecification workAddress = dsm.nodeSpecification( "work address" );
        NodeSpecification homeAddress = dsm.nodeSpecification( "home address" );
        RelationshipSpecification work_address = dsm.relationshipSpecification( "WORK_ADDRESS" );
        RelationshipSpecification home_address = dsm.relationshipSpecification( "HOME_ADDRESS" );

        NodeCollection users = user.create( 1 ).update( dataset );
        users.createRelationshipsTo(
                all(
                        create( workAddress )
                                .relationship( work_address )
                                .relationshipConstraints( exactly( 2 ) ),
                        create( homeAddress )
                                .relationship( home_address )
                                .relationshipConstraints( exactly( 1 ) ) ) )
                .update( dataset );

        dataset.end();

        Node userNode = db.getNodeById( users.getId( 0 ) );

        Iterator<Relationship> workAddressRels = userNode.getRelationships( withName( "WORK_ADDRESS" ) ).iterator();
        Iterator<Relationship> homeAddressRels = userNode.getRelationships( withName( "HOME_ADDRESS" ) ).iterator();

        assertNotNull( workAddressRels.next() );
        assertNotNull( workAddressRels.next() );
        assertFalse( workAddressRels.hasNext() );

        assertNotNull( homeAddressRels.next() );
        assertFalse( homeAddressRels.hasNext() );

        db.shutdown();
    }
}
