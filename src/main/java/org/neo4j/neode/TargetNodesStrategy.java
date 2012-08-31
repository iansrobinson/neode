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
    public static SetRelationshipInfo getExisting( NodeCollection nodeCollection,
                                                   ProbabilityDistribution probabilityDistribution )
    {
        GetExistingUniqueNodes targetNodesSource = new GetExistingUniqueNodes( nodeCollection,
                probabilityDistribution );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetRelationshipInfo getExisting( NodeCollection nodeCollection )
    {
        GetExistingUniqueNodes targetNodesSource = new GetExistingUniqueNodes( nodeCollection,
                normalDistribution() );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetRelationshipInfo getExisting( GraphQuery graphQuery )
    {
        QueryBasedGetExistingNodes targetNodesSource = new QueryBasedGetExistingNodes( graphQuery );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetRelationshipInfo queryBasedGetOrCreate( NodeSpecification nodeSpecification,
                                                             GraphQuery graphQuery )
    {
        QueryBasedGetOrCreateNodes targetNodesSource = new QueryBasedGetOrCreateNodes( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, 1.0, flatDistribution() ) );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetRelationshipInfo queryBasedGetOrCreate( NodeSpecification nodeSpecification, GraphQuery graphQuery,
                                                             double proportionOfCandidateNodesToRequiredNodes )
    {
        QueryBasedGetOrCreateNodes targetNodesSource = new QueryBasedGetOrCreateNodes( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, proportionOfCandidateNodesToRequiredNodes,
                        flatDistribution() ) );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetRelationshipInfo getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities,
                                                   ProbabilityDistribution probabilityDistribution )
    {
        GetOrCreateUniqueNodes targetNodesSource = new GetOrCreateUniqueNodes( nodeSpecification,
                maxNumberOfEntities, probabilityDistribution );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetRelationshipInfo getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities )
    {
        GetOrCreateUniqueNodes targetNodesSource = new GetOrCreateUniqueNodes( nodeSpecification,
                maxNumberOfEntities, flatDistribution() );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetRelationshipInfo create( NodeSpecification nodeSpecification )
    {
        CreateUniqueNodes targetNodesSource = new CreateUniqueNodes( nodeSpecification );
        return new TargetNodesStrategyBuilder( targetNodesSource );
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

    int addRelationshipsToCurrentNode( Node currentNode, NodeCollection targetNodes, int iteration )
    {
        int numberOfRelsToCreate = relationshipConstraints.calculateNumberOfRelsToCreate();
        Iterable<Node> otherNodes = targetNodesSource.getTargetNodes( numberOfRelsToCreate, currentNode );

        int count = 0;
        for ( Node otherNode : otherNodes )
        {
            Relationship relationship = relationshipConstraints
                    .createRelationship( currentNode, otherNode, targetNodes, relationshipInfo, iteration );
            if ( relationship != null )
            {
                count++;
            }
        }
        return count;
    }

    NodeCollection newNodeCollection( GraphDatabaseService db, List<Long> nodeIds )
    {
        return new NodeCollection( db, targetNodesSource.label(), nodeIds );
    }

    NodeCollection newNodeCollection( GraphDatabaseService db, NodeCollectionFactory nodeCollectionFactory )
    {
        return nodeCollectionFactory.createNodeCollection( db, targetNodesSource.label() );
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
}
