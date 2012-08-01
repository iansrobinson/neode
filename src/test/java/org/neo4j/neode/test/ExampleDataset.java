package org.neo4j.neode.test;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.NodeSpecification.createEntities;
import static org.neo4j.neode.NodeSpecification.nodeSpec;
import static org.neo4j.neode.NodeSpecification.relateEntities;
import static org.neo4j.neode.NodeCollection.approxPercent;
import static org.neo4j.neode.commands.GraphQuery.traversal;
import static org.neo4j.neode.commands.RelationshipDescription.getExisting;
import static org.neo4j.neode.commands.RelationshipDescription.getOrCreate;
import static org.neo4j.neode.commands.RelationshipDescription.queryBasedGetOrCreate;
import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.normalDistribution;
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
import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.Dataset;
import org.neo4j.neode.commands.RelationshipUniqueness;
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

        NodeSpecification user = nodeSpec( "user" )
                .withProperties( indexableProperty( "name" ) )
                .build();
        NodeSpecification topic = nodeSpec( "topic" )
                .withProperties( indexableProperty( "label" ) )
                .build();
        NodeSpecification company = nodeSpec( "company" )
                .withProperties( property( "name" ) )
                .build();
        NodeSpecification project = nodeSpec( "project" )
                .withProperties( property( "title" ) )
                .build();

        Dataset dataset = datasetManager.newDataset( "Social network example" );

        NodeCollection users = createEntities( user )
                .quantity( 10 )
                .update( dataset );

        NodeCollection topics = relateEntities( users ).to(
                getOrCreate( topic, 10, normalDistribution() )
                        .relationship( withName( "INTERESTED_IN" ) )
                        .relationshipConstraints( minMax( 1, 3 ) ) )
                .update( dataset );

        relateEntities( users ).to(
                getOrCreate( company, 2, flatDistribution() )
                        .relationship( withName( "WORKS_FOR" ) )
                        .relationshipConstraints( exactly( 1 ) ) )
                .update( dataset );

        NodeCollection allProjects = relateEntities( users ).to(
                queryBasedGetOrCreate( project, traversal( findCompanyProjects ), 1.2 )
                        .relationship( withName( "WORKED_ON" ) )
                        .relationshipConstraints( minMax( 1, 3 ) ) )
                .update( dataset );

        relateEntities( approxPercent( 30, users ) ).to(
                getExisting( allProjects )
                        .relationship( withName( "WORKED_ON" ) )
                        .relationshipConstraints( minMax( 1, 2 ), RelationshipUniqueness.SINGLE_DIRECTION ) )
                .update( dataset );

        dataset.end();

        db.shutdown();

        System.out.println( "Number of topics: " + topics.nodeIds().size() );
    }
}
