package org.neo4j.neode.test;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.neode.GraphQuery.traversal;
import static org.neo4j.neode.Range.exactly;
import static org.neo4j.neode.Range.minMax;
import static org.neo4j.neode.TargetNodesStrategy.getExisting;
import static org.neo4j.neode.TargetNodesStrategy.getOrCreate;
import static org.neo4j.neode.TargetNodesStrategy.queryBasedGetOrCreate;
import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.probabilities.ProbabilityDistribution.normalDistribution;
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
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DatasetManager;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.NodeSpecification;
import org.neo4j.neode.RelationshipSpecification;
import org.neo4j.neode.RelationshipUniqueness;
import org.neo4j.neode.logging.SysOutLog;

public class SocialNetworkExample
{
    @Test
    public void buildSocialNetwork() throws Exception
    {
        GraphDatabaseService db = Db.tempDb();
        DatasetManager dsm = new DatasetManager( db, SysOutLog.INSTANCE );

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

        NodeSpecification user = dsm.nodeSpecification( "user", indexableProperty( "name" ) );
        NodeSpecification topic = dsm.nodeSpecification( "topic", indexableProperty( "label" ) );
        NodeSpecification company = dsm.nodeSpecification( "company", property( "name" ) );
        NodeSpecification project = dsm.nodeSpecification( "project", property( "title" ) );

        RelationshipSpecification interested_in = dsm.relationshipSpecification( "INTERESTED_IN" );
        RelationshipSpecification works_for = dsm.relationshipSpecification( "WORKS_FOR" );
        RelationshipSpecification worked_on = dsm.relationshipSpecification( "WORKED_ON" );

        Dataset dataset = dsm.newDataset( "Social network example" );

        NodeCollection users = user.create( 10 ).update( dataset );

        NodeCollection topics = users.createRelationshipsTo(
                getOrCreate( topic, 10, normalDistribution() )
                        .relationship( interested_in )
                        .relationshipConstraints( minMax( 1, 3 ) ) )
                .update( dataset );

        users.createRelationshipsTo(
                getOrCreate( company, 2, flatDistribution() )
                        .relationship( works_for )
                        .relationshipConstraints( exactly( 1 ) ) )
                .updateNoReturn( dataset );

        NodeCollection allProjects = users.createRelationshipsTo(
                queryBasedGetOrCreate( project, traversal( findCompanyProjects ), 1.2 )
                        .relationship( worked_on )
                        .relationshipConstraints( minMax( 1, 3 ) ) )
                .update( dataset );

        users.approxPercentage( 30 ).createRelationshipsTo(
                getExisting( allProjects )
                        .relationship( worked_on )
                        .relationshipConstraints( minMax( 1, 2 ), RelationshipUniqueness.SINGLE_DIRECTION ) )
                .updateNoReturn( dataset );

        dataset.end();

        db.shutdown();

        System.out.println( "Number of topics: " + topics.size() );
    }
}
