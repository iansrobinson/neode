package org.neo4j.datasetbuilder.test;

import static org.neo4j.datasetbuilder.commands.DomainEntity.domainEntity;
import static org.neo4j.datasetbuilder.commands.DomainEntityBatchCommandBuilder.createEntities;
import static org.neo4j.datasetbuilder.commands.Range.exactly;
import static org.neo4j.datasetbuilder.commands.Range.minMax;
import static org.neo4j.datasetbuilder.commands.RelateNodesBatchCommandBuilder.relateEntities;
import static org.neo4j.datasetbuilder.finders.ContextualGetOrCreate.traversalBasedGetOrCreate;
import static org.neo4j.datasetbuilder.finders.GetOrCreateUniqueNodeFinderStrategy.getOrCreate;
import static org.neo4j.datasetbuilder.numbergenerators.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;
import static org.neo4j.datasetbuilder.numbergenerators.NormalDistributionUniqueRandomNumberGenerator.normalDistribution;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import org.junit.Test;
import org.neo4j.datasetbuilder.BatchCommandExecutor;
import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.SysOutLog;
import org.neo4j.datasetbuilder.commands.DomainEntity;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;

public class ExampleDataset
{
    @Test
    public void buildSocialNetwork() throws Exception
    {
        GraphDatabaseService db = Db.tempDb();
        BatchCommandExecutor executor = new BatchCommandExecutor( db, SysOutLog.INSTANCE );

        TraversalDescription findCompanyProjects = Traversal.description()
                .depthFirst()
                .relationships( withName( "WORKS_FOR" ), Direction.BOTH )
                .relationships( withName( "WORKED_ON" ), Direction.OUTGOING )
                .evaluator( new Evaluator()
                {
                    @Override
                    public Evaluation evaluate( Path path )
                    {
                        if ( path.lastRelationship() == null )
                        {
                            return Evaluation.EXCLUDE_AND_CONTINUE;
                        }
                        if ( path.lastRelationship().isType( withName( "WORKED_ON" ) ) )
                        {
                            return Evaluation.INCLUDE_AND_PRUNE;
                        }
                        return Evaluation.EXCLUDE_AND_CONTINUE;
                    }
                } );

        DomainEntity user = domainEntity( "user", true );
        DomainEntity topic = domainEntity( "topic", "label", true );
        DomainEntity company = domainEntity( "company", true );
        DomainEntity project = domainEntity( "project", "title" );

        DomainEntityInfo users = createEntities( user )
                .quantity( 10 )
                .execute( executor );

        relateEntities( users )
                .to( getOrCreate( topic, 10, normalDistribution() ) )
                .relationship( withName( "INTERESTED_IN" ) )
                .numberOfRels( minMax( 1, 3 ) )
                .execute( executor );

        relateEntities( users )
                .to( getOrCreate( company, 2, flatDistribution() ) )
                .relationship( withName( "WORKS_FOR" ) )
                .numberOfRels( exactly( 1 ) )
                .execute( executor );

        relateEntities( users )
                .to( traversalBasedGetOrCreate( project, findCompanyProjects ) )
                .relationship( withName( "WORKED_ON" ) )
                .numberOfRels( minMax( 1, 3 ) )
                .execute( executor );

        db.shutdown();
    }
}
