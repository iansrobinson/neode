package org.neo4j.datasetbuilder.finders;

import static org.neo4j.datasetbuilder.numbergenerators.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.datasetbuilder.DomainEntity;
import org.neo4j.datasetbuilder.numbergenerators.NumberGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;

public class ContextualTraversalBasedGetOrCreate implements NodeFinderStrategy
{
    public static NodeFinderStrategy traversalBasedGetOrCreate( DomainEntity domainEntity, TraversalDescription
            traversal )
    {
        return new ContextualTraversalBasedGetOrCreate( domainEntity, traversal );
    }

    private final DomainEntity domainEntity;
    private final TraversalDescription traversal;

    private ContextualTraversalBasedGetOrCreate( DomainEntity domainEntity, TraversalDescription traversal )
    {
        this.domainEntity = domainEntity;
        this.traversal = traversal;
    }

    @Override
    public Iterable<Node> getNodes( GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        List<Node> returnNodes = new ArrayList<Node>( numberOfNodes );
        for ( int i = 0; i < numberOfNodes; i++ )
        {
            returnNodes.add( null );
        }

        NumberGenerator numberGenerator = flatDistribution();

        List<Integer> contextIndexes = numberGenerator.generate( numberOfNodes, 0, numberOfNodes - 1, random );
        Traverser traverse = traversal.traverse( currentNode );

        Iterator<Integer> contextIndexesIterator = contextIndexes.iterator();
        Iterator<Node> existingNodesIterator = traverse.nodes().iterator();

        while ( existingNodesIterator.hasNext() && contextIndexesIterator.hasNext() )
        {
            returnNodes.set( contextIndexesIterator.next(), existingNodesIterator.next() );
        }

        List<Integer> currentNodeIndexes = numberGenerator.generate( numberOfNodes, 0, numberOfNodes - 1, random );
        for ( Integer currentNodeIndex : currentNodeIndexes )
        {
            if ( returnNodes.get( currentNodeIndex ) == null )
            {
                Long nodeId = domainEntity.build( db, currentNodeIndex );
                returnNodes.set( currentNodeIndex, db.getNodeById( nodeId ) );
            }
        }

        return returnNodes;
    }

    @Override
    public String entityName()
    {
        return domainEntity.entityName();
    }
}
