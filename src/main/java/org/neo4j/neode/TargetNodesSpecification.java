package org.neo4j.neode;

import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.probabilities.ProbabilityDistribution.normalDistribution;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public class TargetNodesSpecification
{
    public static RelationshipBuilder getExisting( NodeCollection nodeCollection,
                                          ProbabilityDistribution probabilityDistribution )
    {
        return new GetExistingUniqueNodes( nodeCollection, probabilityDistribution );
    }

    public static RelationshipBuilder getExisting( NodeCollection nodeCollection )
    {
        return new GetExistingUniqueNodes( nodeCollection, normalDistribution() );
    }

    public static RelationshipBuilder getExisting( GraphQuery graphQuery )
    {
        return new QueryBasedGetExistingNodes( graphQuery );
    }

    public static RelationshipBuilder queryBasedGetOrCreate( NodeSpecification nodeSpecification, GraphQuery graphQuery )
    {
        return new QueryBasedGetOrCreateNodes( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, 1.0, flatDistribution() ) );
    }

    public static RelationshipBuilder queryBasedGetOrCreate( NodeSpecification nodeSpecification, GraphQuery graphQuery,
                                                    double proportionOfCandidateNodesToRequiredNodes )
    {
        return new QueryBasedGetOrCreateNodes( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, proportionOfCandidateNodesToRequiredNodes,
                        flatDistribution() ) );
    }

    public static RelationshipBuilder getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities,
                                          ProbabilityDistribution probabilityDistribution )
    {
        return new GetOrCreateUniqueNodes( nodeSpecification, maxNumberOfEntities, probabilityDistribution );
    }

    public static RelationshipBuilder getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities )
    {
        return new GetOrCreateUniqueNodes( nodeSpecification, maxNumberOfEntities, flatDistribution() );
    }

    public static RelationshipBuilder create(NodeSpecification nodeSpecification)
    {
        return new CreateUniqueNodes( nodeSpecification );
    }

    private final RelationshipBuilder relationshipBuilder;
    private final RelationshipInfo relationshipInfo;
    private final RelationshipConstraints relationshipConstraints;

    TargetNodesSpecification( RelationshipBuilder relationshipBuilder, RelationshipInfo relationshipInfo,
                              RelationshipConstraints relationshipConstraints )
    {
        this.relationshipBuilder = relationshipBuilder;
        this.relationshipInfo = relationshipInfo;
        this.relationshipConstraints = relationshipConstraints;
    }

    int addRelationshipsToCurrentNode( GraphDatabaseService db, Node currentNode,
                                       NodeIdCollector targetNodeIdCollector, int iteration, Random random )
    {
        int count = 0;
        Iterable<Node> targetNodes = getRandomSelectionOfNodes( db, currentNode, random );
        for ( Node targetNode : targetNodes )
        {
            Relationship relationship = relationshipConstraints
                    .addRelationshipToCurrentNode( currentNode, targetNode, db, targetNodeIdCollector,
                            relationshipInfo, iteration, random );
            if ( relationship != null )
            {
                count++;
            }
        }
        return count;
    }

    NodeCollection newNodeCollection( List<Long> nodeIds )
    {
        return new NodeCollection( relationshipBuilder.label(), nodeIds );
    }

    String createRelationshipDescription( String startNodeLabel )
    {
        return String.format( "(%s)%s(%s)",
                startNodeLabel, relationshipInfo.description(), relationshipBuilder.label() );
    }

    String createRelationshipConstraintsDescription()
    {
        return relationshipConstraints.description();
    }

    private Iterable<Node> getRandomSelectionOfNodes( GraphDatabaseService db, Node firstNode, Random random )
    {
        int numberOfRelsToCreate = relationshipConstraints.calculateNumberOfRelsToCreate( random );
        return relationshipBuilder.getNodes( numberOfRelsToCreate, db, firstNode, random );
    }

}
