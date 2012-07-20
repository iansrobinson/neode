package org.neo4j.datasetbuilder.test;

import static org.neo4j.datasetbuilder.commands.DomainEntityBatchCommandBuilder.createEntities;
import static org.neo4j.datasetbuilder.commands.DomainEntityBuilder.domainEntity;
import static org.neo4j.datasetbuilder.commands.RelateNodesBatchCommandBuilder.relateEntities;
import static org.neo4j.datasetbuilder.finders.GetOrCreateUniqueNodeFinderStrategy.getOrCreate;
import static org.neo4j.datasetbuilder.randomnumbers.NormalDistributionUniqueRandomNumberGenerator.normalDistribution;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import org.junit.Test;
import org.neo4j.datasetbuilder.BatchCommandExecutor;
import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.SysOutLog;
import org.neo4j.datasetbuilder.commands.DomainEntityBuilder;
import org.neo4j.graphdb.GraphDatabaseService;

public class ExampleDataset
{
    @Test
    public void buildSocialNetwork() throws Exception
    {
        GraphDatabaseService db = Db.tempDb();
        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );

        DomainEntityBuilder user = domainEntity( "user", true );
        DomainEntityBuilder topic = domainEntity( "topic", "label", true );
        DomainEntityBuilder company = domainEntity( "company", true );

        DomainEntityInfo users = createEntities( user )
                .quantity( 100 )
                .batchSize( 500 )
                .execute( executor );

        relateEntities( users )
                .to( topic, getOrCreate( 10, normalDistribution() ) )
                .relationship( withName( "INTERESTED_IN" ) )
                .batchSize( 500 )
                .minMaxNumberOfRels( 3, 10 )
                .execute( executor );

        relateEntities( users )
                .to( company, getOrCreate( 20 ) )
                .relationship( withName( "WORKS_FOR" ) )
                .batchSize( 500 )
                .minMaxNumberOfRels( 1, 1 )
                .execute( executor );

        db.shutdown();
    }
}
