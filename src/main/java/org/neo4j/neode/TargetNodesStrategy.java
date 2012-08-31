package org.neo4j.neode;

import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.probabilities.ProbabilityDistribution.normalDistribution;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.interfaces.SetRelationshipInfo;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public class TargetNodesStrategy
{
    public static SetRelationshipInfo getExisting( NodeIdCollection nodeIdCollection,
                                                   ProbabilityDistribution probabilityDistribution )
    {
        return new GetExistingUniqueNodes( nodeIdCollection, probabilityDistribution );
    }

    public static SetRelationshipInfo getExisting( NodeIdCollection nodeIdCollection )
    {
        return new GetExistingUniqueNodes( nodeIdCollection, normalDistribution() );
    }

    public static SetRelationshipInfo getExisting( GraphQuery graphQuery )
    {
        return new QueryBasedGetExistingNodes( graphQuery );
    }

    public static SetRelationshipInfo queryBasedGetOrCreate( NodeSpecification nodeSpecification,
                                                             GraphQuery graphQuery )
    {
        return new QueryBasedGetOrCreateNodes( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, 1.0, flatDistribution() ) );
    }

    public static SetRelationshipInfo queryBasedGetOrCreate( NodeSpecification nodeSpecification, GraphQuery graphQuery,
                                                             double proportionOfCandidateNodesToRequiredNodes )
    {
        return new QueryBasedGetOrCreateNodes( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, proportionOfCandidateNodesToRequiredNodes,
                        flatDistribution() ) );
    }

    public static SetRelationshipInfo getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities,
                                                   ProbabilityDistribution probabilityDistribution )
    {
        return new GetOrCreateUniqueNodes( nodeSpecification, maxNumberOfEntities, probabilityDistribution );
    }

    public static SetRelationshipInfo getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities )
    {
        return new GetOrCreateUniqueNodes( nodeSpecification, maxNumberOfEntities, flatDistribution() );
    }

    public static SetRelationshipInfo create( NodeSpecification nodeSpecification )
    {
        return new CreateUniqueNodes( nodeSpecification );
    }

    private final TargetNodesSource targetNodesSource;
    private final RelationshipInfo relationshipInfo;
    private final RelationshipConstraints relationshipConstraints;

    TargetNodesStrategy( TargetNodesSource targetNodesSource, RelationshipInfo relationshipInfo,
                         RelationshipConstraints relationshipConstraints )
    {
        this.targetNodesSource = targetNodesSource;
        this.relationshipInfo = relationshipInfo;
        this.relationshipConstraints = relationshipConstraints;
    }

    int addRelationshipsToCurrentNode( GraphDatabaseService db, Node currentNode, NodeIdCollection targetNodeIds,
                                       int iteration )
    {
        int count = 0;
        Iterable<Node> targetNodes = getRandomSelectionOfNodes( db, currentNode );
        for ( Node targetNode : targetNodes )
        {
            Relationship relationship = relationshipConstraints
                    .addRelationshipToCurrentNode( currentNode, targetNode, targetNodeIds,
                            relationshipInfo, iteration );
            if ( relationship != null )
            {
                count++;
            }
        }
        return count;
    }

    NodeIdCollection newNodeIdCollection( List<Long> nodeIds )
    {
        return new NodeIdCollection( targetNodesSource.label(), nodeIds );
    }

    NodeIdCollection newNodeIdCollection( NodeIdCollectionFactory nodeIdCollectionFactory )
    {
        return nodeIdCollectionFactory.createNodeIdCollection( targetNodesSource.label() );
    }

    String description( String startNodeLabel )
    {
        return String.format( "(%s)%s(%s)",
                startNodeLabel, relationshipInfo.description(), targetNodesSource.label() );
    }

    String relationshipConstraintsDescription()
    {
        return relationshipConstraints.description();
    }

    private Iterable<Node> getRandomSelectionOfNodes( GraphDatabaseService db, Node firstNode )
    {
        int numberOfRelsToCreate = relationshipConstraints.calculateNumberOfRelsToCreate();
        return targetNodesSource.getTargetNodes( numberOfRelsToCreate, db, firstNode );
    }

}
