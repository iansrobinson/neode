package org.neo4j.neode;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.probabilities.ProbabilityDistribution;

class RelationshipConstraints
{
    private final Range cardinality;
    private final RelationshipUniqueness relationshipUniqueness;
    private final ProbabilityDistribution probabilityDistribution;

    RelationshipConstraints( Range cardinality, RelationshipUniqueness relationshipUniqueness,
                             ProbabilityDistribution probabilityDistribution )
    {
        this.cardinality = cardinality;
        this.relationshipUniqueness = relationshipUniqueness;
        this.probabilityDistribution = probabilityDistribution;
    }

    public int calculateNumberOfRelsToCreate()
    {
        return probabilityDistribution.generateSingle( cardinality );
    }

    public Relationship createRelationship( Node currentNode, Node targetNode,
                                            NodeIdCollection targetNodeIds,
                                            RelationshipInfo relationshipInfo, int iteration )
    {
        Relationship relationship = relationshipUniqueness.createRelationship( currentNode, targetNode,
                relationshipInfo, iteration );
        if ( relationship != null )
        {
            targetNodeIds.add( targetNode.getId() );
        }
        return relationship;
    }

    public String description()
    {
        return String.format( "[Min: %s, Max: %s, Uniqueness: %s, Distribution: %s]",
                cardinality.min(),
                cardinality.max(),
                relationshipUniqueness.name(),
                probabilityDistribution.description() );
    }


}
