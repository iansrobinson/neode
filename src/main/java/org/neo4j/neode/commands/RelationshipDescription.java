package org.neo4j.neode.commands;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.DomainEntityInfo;

public class RelationshipDescription
{
    public static RelationshipDescriptionBuilder entities( NodeFinder nodes )
    {
        return new RelationshipDescriptionBuilder( nodes );
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
                    .addRelationshipToCurrentNode( db, currentNode, targetNode, targetNodeIdCollector,
                            relationshipInfo );
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
        return nodeFinder.getNodes( db, firstNode, numberOfRelsToCreate, random );
    }

}
