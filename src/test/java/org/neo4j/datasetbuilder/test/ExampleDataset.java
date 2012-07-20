package org.neo4j.datasetbuilder.test;

import static org.neo4j.datasetbuilder.commands.DomainEntityBatchCommandBuilder.createEntities;
import static org.neo4j.datasetbuilder.commands.DomainEntity.domainEntity;
import static org.neo4j.datasetbuilder.commands.MinMax.exactly;
import static org.neo4j.datasetbuilder.commands.MinMax.minMax;
import static org.neo4j.datasetbuilder.commands.RelateNodesBatchCommandBuilder.relateEntities;
import static org.neo4j.datasetbuilder.finders.GetOrCreateUniqueNodeFinderStrategy.getOrCreate;
import static org.neo4j.datasetbuilder.randomnumbers.NormalDistributionUniqueRandomNumberGenerator.normalDistribution;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import org.junit.Test;
import org.neo4j.datasetbuilder.BatchCommandExecutor;
import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.SysOutLog;
import org.neo4j.datasetbuilder.commands.DomainEntity;
import org.neo4j.graphdb.GraphDatabaseService;

public class ExampleDataset
{
    @Test
    public void buildSocialNetwork() throws Exception
    {
        GraphDatabaseService db = Db.tempDb();
        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );

        DomainEntity user = domainEntity( "user", true );
        DomainEntity topic = domainEntity( "topic", "label", true );
        DomainEntity company = domainEntity( "company", true );

        DomainEntityInfo users = createEntities( user )
                .quantity( 100 )
                .execute( executor );

        relateEntities( users )
                .to( getOrCreate( topic, 10, normalDistribution() ) )
                .relationship( withName( "INTERESTED_IN" ) )
                .numberOfRels( minMax( 3, 10 ) )
                .execute( executor );

        relateEntities( users )
                .to( getOrCreate( company, 20 ) )
                .relationship( withName( "WORKS_FOR" ) )
                .numberOfRels( exactly( 1 ) )
                .execute( executor );

        db.shutdown();
    }
}
