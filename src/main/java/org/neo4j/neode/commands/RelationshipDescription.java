package org.neo4j.neode.commands;

import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.normalDistribution;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;

public class RelationshipDescription
{
    public static NodeFinder getExisting( DomainEntityInfo domainEntities, ProbabilityDistribution
            probabilityDistribution )
    {
        return new ExistingUniqueNodeFinder( domainEntities, probabilityDistribution );
    }

    public static NodeFinder getExisting( DomainEntityInfo domainEntities )
    {
        return new ExistingUniqueNodeFinder( domainEntities, normalDistribution() );
    }

    public static NodeFinder queryBasedGetOrCreate( DomainEntity domainEntity, GraphQuery graphQuery )
    {
        return new QueryBasedGetOrCreate( domainEntity,
                new SparseNodeListGenerator( graphQuery, 1.0, flatDistribution() ) );
    }

    public static NodeFinder queryBasedGetOrCreate( DomainEntity domainEntity, GraphQuery graphQuery,
                                                    double proportionOfCandidateNodesToRequiredNodes )
    {
        return new QueryBasedGetOrCreate( domainEntity,
                new SparseNodeListGenerator( graphQuery, proportionOfCandidateNodesToRequiredNodes,
                        flatDistribution() ) );
    }

    public static NodeFinder getOrCreate( DomainEntity domainEntity, int maxNumberOfEntities,
                                          ProbabilityDistribution probabilityDistribution )
    {
        return new GetOrCreateUniqueNodeFinder( domainEntity, maxNumberOfEntities, probabilityDistribution );
    }

    public static NodeFinder getOrCreate( DomainEntity domainEntity, int maxNumberOfEntities )
    {
        return new GetOrCreateUniqueNodeFinder( domainEntity, maxNumberOfEntities, flatDistribution() );
    }

    private final NodeFinder nodeFinder;
    private final RelationshipInfo relationshipInfo;
    private final RelationshipConstraints relationshipConstraints;

    RelationshipDescription( NodeFinder nodeFinder, RelationshipInfo relationshipInfo,
                             RelationshipConstraints relationshipConstraints )
    {
        this.nodeFinder = nodeFinder;
        this.relationshipInfo = relationshipInfo;
        this.relationshipConstraints = relationshipConstraints;
    }

    int addRelationshipsToCurrentNode( GraphDatabaseService db, Node currentNode,
                                       NodeIdCollector targetNodeIdCollector, Random random )
    {
        int count = 0;
        Iterable<Node> targetNodes = getRandomSelectionOfNodes( db, currentNode, random );
        for ( Node targetNode : targetNodes )
        {
            Relationship relationship = relationshipConstraints
                    .addRelationshipToCurrentNode( currentNode, targetNode, db, targetNodeIdCollector,
                            relationshipInfo, random );
            if ( relationship != null )
            {
                count++;
            }
        }
        return count;
    }

    DomainEntityInfo newDomainEntityInfo( List<Long> nodeIds )
    {
        return new DomainEntityInfo( nodeFinder.entityName(), nodeIds );
    }

    String createRelationshipDescription( String startNodeLabel )
    {
        return String.format( "(%s)%s(%s)",
                startNodeLabel, relationshipInfo.description(), nodeFinder.entityName() );
    }

    String createRelationshipConstraintsDescription()
    {
        return relationshipConstraints.description();
    }

    private Iterable<Node> getRandomSelectionOfNodes( GraphDatabaseService db, Node firstNode, Random random )
    {
        int numberOfRelsToCreate = relationshipConstraints.calculateNumberOfRelsToCreate( random );
        return nodeFinder.getNodes( numberOfRelsToCreate, db, firstNode, random );
    }

}
