package org.neo4j.neode;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.Log;

class RelateNodesBatchCommand implements BatchCommand<NodeCollection>
{
    private final NodeCollection sourceNodes;
    private final TargetNodesStrategy targetNodesStrategy;
    private final NodeCollection targetNodes;
    private final int batchSize;
    private long totalRels = 0;

    RelateNodesBatchCommand( NodeCollection sourceNodes, TargetNodesStrategy targetNodesStrategy,
                             NodeCollection targetNodes, int batchSize )
    {
        this.sourceNodes = sourceNodes;
        this.targetNodesStrategy = targetNodesStrategy;
        this.targetNodes = targetNodes;
        this.batchSize = batchSize;
    }

    @Override
    public int numberOfIterations()
    {
        return sourceNodes.size();
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( int iteration )
    {
        Node currentNode = sourceNodes.getNodeByPosition( iteration );
        totalRels += targetNodesStrategy
                .addRelationshipsToCurrentNode( currentNode, targetNodes, iteration );
    }

    @Override
    public String description()
    {
        return String.format( "Creating '%s' relationships.", shortDescription() );
    }

    @Override
    public String shortDescription()
    {
        return targetNodesStrategy.description( sourceNodes.labelName() );
    }

    @Override
    public void onBegin( Log log )
    {
        log.write( String.format( "      %s", targetNodesStrategy.relationshipConstraintsDescription() ) );
    }

    @Override
    public void onEnd( Log log )
    {
        long average = sourceNodes.size() == 0 ? 0 : totalRels / sourceNodes.size();
        log.write( String.format( "      [Avg: %s relationship(s) per %s]",
                average, sourceNodes.labelName() ) );
    }

    @Override
    public NodeCollection results()
    {
        return targetNodes;
    }
}
