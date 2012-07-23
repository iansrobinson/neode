package org.neo4j.datasetbuilder.test;

import static org.neo4j.datasetbuilder.DomainEntityBuilder.domainEntity;
import static org.neo4j.datasetbuilder.DomainEntityInfo.approxPercent;
import static org.neo4j.datasetbuilder.commands.DomainEntityBatchCommandBuilder.createEntities;
import static org.neo4j.datasetbuilder.commands.Range.exactly;
import static org.neo4j.datasetbuilder.commands.Range.minMax;
import static org.neo4j.datasetbuilder.commands.RelateNodesBatchCommandBuilder.relateEntities;
import static org.neo4j.datasetbuilder.commands.Unique.unique;
import static org.neo4j.datasetbuilder.finders.ContextualTraversalBasedGetOrCreate.traversalBasedGetOrCreate;
import static org.neo4j.datasetbuilder.finders.ExistingUniqueNodeFinderStrategy.getExisting;
import static org.neo4j.datasetbuilder.finders.GetOrCreateUniqueNodeFinderStrategy.getOrCreate;
import static org.neo4j.datasetbuilder.numbergenerators.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;
import static org.neo4j.datasetbuilder.numbergenerators.NormalDistributionUniqueRandomNumberGenerator
        .normalDistribution;
import static org.neo4j.datasetbuilder.properties.Property.indexableProperty;
import static org.neo4j.datasetbuilder.properties.Property.property;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import org.junit.Test;
import org.neo4j.datasetbuilder.Dataset;
import org.neo4j.datasetbuilder.DatasetManager;
import org.neo4j.datasetbuilder.DomainEntity;
import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.logging.SysOutLog;
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
        DatasetManager datasetManager = new DatasetManager( db, SysOutLog.INSTANCE );

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

        DomainEntity user = domainEntity( "user" )
                .withProperties( indexableProperty( "name" ) )
                .build();
        DomainEntity topic = domainEntity( "topic" )
                .withProperties( indexableProperty( "label" ) )
                .build();
        DomainEntity company = domainEntity( "company" )
                .withProperties( indexableProperty( "name" ) )
                .build();
        DomainEntity project = domainEntity( "project" )
                .withProperties( property( "title" ) )
                .build();

        Dataset dataset = datasetManager.newDataset( "Create social network example" );

        DomainEntityInfo users = createEntities( user )
                .quantity( 10 )
                .addTo( dataset );

        relateEntities( users )
                .to( getOrCreate( topic, 10, normalDistribution() ) )
                .relationship( withName( "INTERESTED_IN" ) )
                .cardinality( minMax( 1, 3 ) )
                .addTo( dataset );

        relateEntities( users )
                .to( getOrCreate( company, 2, flatDistribution() ) )
                .relationship( withName( "WORKS_FOR" ) )
                .cardinality( exactly( 1 ) )
                .addTo( dataset );

        DomainEntityInfo allProjects = relateEntities( users )
                .to( traversalBasedGetOrCreate( project, findCompanyProjects ) )
                .relationship( withName( "WORKED_ON" ) )
                .cardinality( minMax( 1, 3 ) )
                .addTo( dataset );

        relateEntities( approxPercent( 30, users ) )
                .to( getExisting( allProjects ) )
                .relationship( withName( "WORKS_FOR" ) )
                .cardinality( minMax( 1, 2 ), unique() )
                .addTo( dataset );

        dataset.end();

        db.shutdown();
    }
}
