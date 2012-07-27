package org.neo4j.neode.commands;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;
import org.neo4j.neode.numbergenerators.Range;

class RelationshipConstraints
{
    private final Range cardinality;
    private final RelationshipUniqueness relationshipUniqueness;
    private final ProbabilityDistribution probabilityDistribution;

    public RelationshipConstraints( Range cardinality,
                                    RelationshipUniqueness relationshipUniqueness )
    {
        this.cardinality = cardinality;
        this.relationshipUniqueness = relationshipUniqueness;
        this.probabilityDistribution = ProbabilityDistribution.flatDistribution();
    }

    public int calculateNumberOfRelsToCreate( Random random )
    {
        return probabilityDistribution.generateSingle( cardinality, random );
    }

    public Relationship addRelationshipToCurrentNode( Node currentNode, Node targetNode, GraphDatabaseService db,
                                                      NodeIdCollector targetNodeIdCollector,
                                                      RelationshipInfo relationshipInfo, Random random )
    {
        Relationship relationship = relationshipUniqueness.createRelationship( db, currentNode, targetNode,
                relationshipInfo, random );
        if ( relationship != null )
        {
            targetNodeIdCollector.add( targetNode.getId() );
        }
        return relationship;
    }

    public String description()
    {
        return String.format( "[Min: %s, Max: %s, Uniqueness: %s]", cardinality.min(), cardinality.max(),
                relationshipUniqueness.name() );
    }


}
