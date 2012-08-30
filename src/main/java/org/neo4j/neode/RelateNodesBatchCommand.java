package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.Log;

class RelateNodesBatchCommand implements BatchCommand<NodeIdCollection>
{
    private final NodeIdCollection startNodeIds;
    private final CreateRelationshipSpecification createRelationshipSpecification;
    private final NodeIdCollector nodeIdCollector;
    private final int batchSize;
    private long totalRels = 0;

    RelateNodesBatchCommand( NodeIdCollection startNodeIds, CreateRelationshipSpecification createRelationshipSpecification,
                             NodeIdCollector nodeIdCollector, int batchSize )
    {
        this.startNodeIds = startNodeIds;
        this.createRelationshipSpecification = createRelationshipSpecification;
        this.nodeIdCollector = nodeIdCollector;
        this.batchSize = batchSize;
    }

    @Override
    public int numberOfIterations()
    {
        return startNodeIds.size();
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( GraphDatabaseService db, int iteration, Random random )
    {
        Node currentNode = db.getNodeById( startNodeIds.getIdByPosition( iteration ) );
        execute( currentNode, db, iteration, random );
    }

    @Override
    public void execute( Node currentNode, GraphDatabaseService db, int iteration, Random random )
    {
        totalRels += createRelationshipSpecification
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
        return createRelationshipSpecification.createRelationshipDescription( startNodeIds.label() );
    }

    @Override
    public void onBegin( Log log )
    {
        log.write( String.format( "      %s", createRelationshipSpecification.createRelationshipConstraintsDescription() ) );
    }

    @Override
    public void onEnd( Log log )
    {
        log.write( String.format( "      [Avg: %s relationship(s) per %s]",
                totalRels / startNodeIds.size(), startNodeIds.label() ) );
    }

    @Override
    public NodeIdCollection results()
    {
        return createRelationshipSpecification.newNodeIdCollection( nodeIdCollector.nodeIds() );
    }
}
