package org.neo4j.neode.test;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.DomainEntity.createEntities;
import static org.neo4j.neode.DomainEntity.domainEntity;
import static org.neo4j.neode.DomainEntity.relateEntities;
import static org.neo4j.neode.DomainEntityInfo.approxPercent;
import static org.neo4j.neode.commands.GraphQuery.traversal;
import static org.neo4j.neode.commands.NodeFinder.contextualGetOrCreate;
import static org.neo4j.neode.commands.NodeFinder.getExisting;
import static org.neo4j.neode.commands.NodeFinder.getOrCreate;
import static org.neo4j.neode.commands.RelationshipDescription.entities;
import static org.neo4j.neode.numbergenerators.Distribution.flatDistribution;
import static org.neo4j.neode.numbergenerators.Distribution.normalDistribution;
import static org.neo4j.neode.numbergenerators.Range.exactly;
import static org.neo4j.neode.numbergenerators.Range.minMax;
import static org.neo4j.neode.properties.Property.indexableProperty;
import static org.neo4j.neode.properties.Property.property;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.commands.Dataset;
import org.neo4j.neode.commands.Uniqueness;
import org.neo4j.neode.logging.SysOutLog;

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
                .withProperties( property( "name" ) )
                .build();
        DomainEntity project = domainEntity( "project" )
                .withProperties( property( "title" ) )
                .build();

        Dataset dataset = datasetManager.newDataset( "Social network example" );

        DomainEntityInfo users = createEntities( user )
                .quantity( 10 )
                .update( dataset );

        DomainEntityInfo topics = relateEntities( users ).to(
                entities( getOrCreate( topic, 10, normalDistribution() ) )
                        .relationship( withName( "INTERESTED_IN" ) )
                        .relationshipConstraints( minMax( 1, 3 ) ) )
                .update( dataset );

        relateEntities( users ).to(
                entities( getOrCreate( company, 2, flatDistribution() ) )
                        .relationship( withName( "WORKS_FOR" ) )
                        .relationshipConstraints( exactly( 1 ) ) )
                .update( dataset );

        DomainEntityInfo allProjects = relateEntities( users ).to(
                entities( contextualGetOrCreate( project, traversal( findCompanyProjects ), 1.2 ) )
                        .relationship( withName( "WORKED_ON" ) )
                        .relationshipConstraints( minMax( 1, 3 ) ) )
                .update( dataset );

        relateEntities( approxPercent( 30, users ) ).to(
                entities( getExisting( allProjects ) )
                        .relationship( withName( "WORKED_ON" ) )
                        .relationshipConstraints( minMax( 1, 2 ), Uniqueness.SINGLE_DIRECTION ) )
                .update( dataset );

        dataset.end();

        db.shutdown();

        System.out.println( "Number of topics: " + topics.nodeIds().size() );
    }
}
