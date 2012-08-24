package org.neo4j.neode;

import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.probabilities.ProbabilityDistribution.normalDistribution;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.interfaces.SetRelationshipInfo;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

public class CreateRelationshipSpecification
{
    public static SetRelationshipInfo getExisting( NodeCollection nodeCollection,
                                          ProbabilityDistribution probabilityDistribution )
    {
        return new GetExistingUniqueNodes( nodeCollection, probabilityDistribution );
    }

    public static SetRelationshipInfo getExisting( NodeCollection nodeCollection )
    {
        return new GetExistingUniqueNodes( nodeCollection, normalDistribution() );
    }

    public static SetRelationshipInfo getExisting( GraphQuery graphQuery )
    {
        return new QueryBasedGetExistingNodes( graphQuery );
    }

    public static SetRelationshipInfo queryBasedGetOrCreate( NodeSpecification nodeSpecification, GraphQuery graphQuery )
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

    public static SetRelationshipInfo create(NodeSpecification nodeSpecification)
    {
        return new CreateUniqueNodes( nodeSpecification );
    }

    private final NodeSource nodeSource;
    private final RelationshipInfo relationshipInfo;
    private final RelationshipConstraints relationshipConstraints;

    CreateRelationshipSpecification( NodeSource nodeSource, RelationshipInfo relationshipInfo,
                                     RelationshipConstraints relationshipConstraints )
    {
        this.nodeSource = nodeSource;
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
        return new NodeCollection( nodeSource.label(), nodeIds );
    }

    String createRelationshipDescription( String startNodeLabel )
    {
        return String.format( "(%s)%s(%s)",
                startNodeLabel, relationshipInfo.description(), nodeSource.label() );
    }

    String createRelationshipConstraintsDescription()
    {
        return relationshipConstraints.description();
    }

    private Iterable<Node> getRandomSelectionOfNodes( GraphDatabaseService db, Node firstNode, Random random )
    {
        int numberOfRelsToCreate = relationshipConstraints.calculateNumberOfRelsToCreate( random );
        return nodeSource.getNodes( numberOfRelsToCreate, db, firstNode, random );
    }

}
