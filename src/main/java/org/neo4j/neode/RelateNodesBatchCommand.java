package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.Log;

class RelateNodesBatchCommand implements BatchCommand<NodeCollection>
{
    private final NodeCollection startNodes;
    private final TargetNodesSpecification targetNodesSpecification;
    private final NodeIdCollector nodeIdCollector;
    private final int batchSize;
    private long totalRels = 0;

    RelateNodesBatchCommand( NodeCollection startNodes, TargetNodesSpecification targetNodesSpecification,
                             NodeIdCollector nodeIdCollector, int batchSize )
    {
        this.startNodes = startNodes;
        this.targetNodesSpecification = targetNodesSpecification;
        this.nodeIdCollector = nodeIdCollector;
        this.batchSize = batchSize;
    }

    @Override
    public int numberOfIterations()
    {
        return startNodes.size();
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( GraphDatabaseService db, int iteration, Random random )
    {
        Node currentNode = db.getNodeById( startNodes.nodeIds().get( iteration ) );
        execute( currentNode, db, iteration, random );
    }

    @Override
    public void execute( Node currentNode, GraphDatabaseService db, int iteration, Random random )
    {
        totalRels += targetNodesSpecification
                .addRelationshipsToCurrentNode( db, currentNode, nodeIdCollector, iteration, random );
    }

    @Override
    public String description()
    {
        return String.format( "Creating '%s' relationships.", shortDescription() );
    }

    @Override
    public String shortDescription()
    {
        return targetNodesSpecification.createRelationshipDescription( startNodes.label() );
    }

    @Override
    public void onBegin( Log log )
    {
        log.write( String.format( "      %s", targetNodesSpecification.createRelationshipConstraintsDescription() ) );
    }

    @Override
    public void onEnd( Log log )
    {
        log.write( String.format( "      [Avg: %s relationship(s) per %s]",
                totalRels / startNodes.nodeIds().size(), startNodes.label() ) );
    }

    @Override
    public NodeCollection results()
    {
        return targetNodesSpecification.newNodeCollection( nodeIdCollector.nodeIds() );
    }
}
