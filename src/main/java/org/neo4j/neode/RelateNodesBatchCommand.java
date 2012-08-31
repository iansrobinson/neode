package org.neo4j.neode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.Log;

class RelateNodesBatchCommand implements BatchCommand<NodeIdCollection>
{
    private final NodeIdCollection sourceNodeIds;
    private final TargetNodesStrategy targetNodesStrategy;
    private final NodeIdCollection targetNodeIds;
    private final int batchSize;
    private long totalRels = 0;

    RelateNodesBatchCommand( NodeIdCollection sourceNodeIds, TargetNodesStrategy targetNodesStrategy,
                             NodeIdCollection targetNodeIds, int batchSize )
    {
        this.sourceNodeIds = sourceNodeIds;
        this.targetNodesStrategy = targetNodesStrategy;
        this.targetNodeIds = targetNodeIds;
        this.batchSize = batchSize;
    }

    @Override
    public int numberOfIterations()
    {
        return sourceNodeIds.size();
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( GraphDatabaseService db, int iteration )
    {
        Node currentNode = db.getNodeById( sourceNodeIds.getIdByPosition( iteration ) );
        totalRels += targetNodesStrategy
                .addRelationshipsToCurrentNode( db, currentNode, targetNodeIds, iteration );
    }

    @Override
    public String description()
    {
        return String.format( "Creating '%s' relationships.", shortDescription() );
    }

    @Override
    public String shortDescription()
    {
        return targetNodesStrategy.description( sourceNodeIds.label() );
    }

    @Override
    public void onBegin( Log log )
    {
        log.write( String.format( "      %s", targetNodesStrategy.relationshipConstraintsDescription() ) );
    }

    @Override
    public void onEnd( Log log )
    {
        log.write( String.format( "      [Avg: %s relationship(s) per %s]",
                totalRels / sourceNodeIds.size(), sourceNodeIds.label() ) );
    }

    @Override
    public NodeIdCollection results()
    {
        return targetNodeIds;
    }
}
