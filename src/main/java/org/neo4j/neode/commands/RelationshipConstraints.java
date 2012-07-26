package org.neo4j.neode.commands;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.numbergenerators.Distribution;
import org.neo4j.neode.numbergenerators.Range;

class RelationshipConstraints
{
    private final Range cardinality;
    private final Uniqueness uniqueness;
    private final Distribution distribution;

    public RelationshipConstraints( Range cardinality,
                                    Uniqueness uniqueness )
    {
        this.cardinality = cardinality;
        this.uniqueness = uniqueness;
        this.distribution = Distribution.flatDistribution();
    }

    public int calculateNumberOfRelsToCreate(Random random)
    {
        return distribution.generateSingle( cardinality, random );
    }

    public Relationship addRelationshipToCurrentNode( GraphDatabaseService db, Node firstNode, Node targetNode,
                                                      NodeIdCollector targetNodeIdCollector,
                                                      RelationshipInfo relationshipInfo )
    {
        Relationship relationship = uniqueness.createRelationship( db, firstNode, targetNode, relationshipInfo );
        if ( relationship != null )
        {
            targetNodeIdCollector.add( targetNode.getId() );
        }
        return relationship;
    }

    public String description()
    {
        return String.format( "[Min: %s, Max: %s, Uniqueness: %s]", cardinality.min(), cardinality.max(),
                uniqueness.name() );
    }


}
