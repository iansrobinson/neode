package org.neo4j.neode;

import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.probabilities.ProbabilityDistribution.normalDistribution;

import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.interfaces.SetNumberOfNodes;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public class TargetNodesStrategy
{
    public static SetNumberOfNodes getExisting( NodeCollection nodeCollection,
                                                   ProbabilityDistribution probabilityDistribution )
    {
        GetExistingUniqueNodes targetNodesSource = new GetExistingUniqueNodes( nodeCollection,
                probabilityDistribution );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetNumberOfNodes getExisting( NodeCollection nodeCollection )
    {
        GetExistingUniqueNodes targetNodesSource = new GetExistingUniqueNodes( nodeCollection,
                normalDistribution() );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetNumberOfNodes getExisting( GraphQuery graphQuery )
    {
        QueryBasedGetExistingNodes targetNodesSource = new QueryBasedGetExistingNodes( graphQuery );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetNumberOfNodes queryBasedGetOrCreate( NodeSpecification nodeSpecification,
                                                             GraphQuery graphQuery )
    {
        QueryBasedGetOrCreateNodes targetNodesSource = new QueryBasedGetOrCreateNodes( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, 1.0, flatDistribution() ) );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetNumberOfNodes queryBasedGetOrCreate( NodeSpecification nodeSpecification, GraphQuery graphQuery,
                                                             double proportionOfCandidateNodesToRequiredNodes )
    {
        QueryBasedGetOrCreateNodes targetNodesSource = new QueryBasedGetOrCreateNodes( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, proportionOfCandidateNodesToRequiredNodes,
                        flatDistribution() ) );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetNumberOfNodes getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities,
                                                   ProbabilityDistribution probabilityDistribution )
    {
        GetOrCreateUniqueNodes targetNodesSource = new GetOrCreateUniqueNodes( nodeSpecification,
                maxNumberOfEntities, probabilityDistribution );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetNumberOfNodes getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities )
    {
        GetOrCreateUniqueNodes targetNodesSource = new GetOrCreateUniqueNodes( nodeSpecification,
                maxNumberOfEntities, flatDistribution() );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    public static SetNumberOfNodes create( NodeSpecification nodeSpecification )
    {
        CreateUniqueNodes targetNodesSource = new CreateUniqueNodes( nodeSpecification );
        return new TargetNodesStrategyBuilder( targetNodesSource );
    }

    private final TargetNodesSource targetNodesSource;
    private final Range targetNodesRange;
    private final ProbabilityDistribution targetNodesProbabilityDistribution;
    private final RelationshipInfo relationshipInfo;
    private final RelationshipConstraints relationshipConstraints;

    TargetNodesStrategy( TargetNodesSource targetNodesSource,
                         Range targetNodesRange,
                         ProbabilityDistribution targetNodesProbabilityDistribution,
                         RelationshipInfo relationshipInfo,
                         RelationshipConstraints relationshipConstraints )
    {
        this.targetNodesSource = targetNodesSource;
        this.targetNodesRange = targetNodesRange;
        this.relationshipInfo = relationshipInfo;
        this.relationshipConstraints = relationshipConstraints;
        this.targetNodesProbabilityDistribution = targetNodesProbabilityDistribution;
    }

    int addRelationshipsToCurrentNode( Node currentNode, NodeCollection targetNodes, int iteration )
    {
        int numberOfNodesToCreate = targetNodesRange.getRandom( targetNodesProbabilityDistribution );
        Iterable<Node> otherNodes = targetNodesSource.getTargetNodes( numberOfNodesToCreate, currentNode );

        int relationshipCount = 0;
        for ( Node otherNode : otherNodes )
        {
            int numberOfRelsPerNode = relationshipConstraints.calculateNumberOfRelsToCreate();
            for (int relationshipIndex = 0; relationshipIndex < numberOfRelsPerNode; relationshipIndex++)
            {
                Relationship relationship = relationshipConstraints
                        .createRelationship( currentNode, otherNode, targetNodes, relationshipInfo, iteration );
                if ( relationship != null )
                {
                    relationshipCount++;
                }
            }
        }
        return relationshipCount;
    }

    NodeCollection newNodeCollection( GraphDatabaseService db, Set<Long> nodeIds )
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
