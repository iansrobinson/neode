package org.neo4j.neode.commands;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.logging.Log;

class RelateNodesBatchCommand implements BatchCommand<NodeCollection>
{
    private final NodeCollection startNodes;
    private final RelationshipDescription relationshipDescription;
    private final NodeIdCollector targetNodeIdCollector;
    private final int batchSize;
    private long totalRels = 0;

    public RelateNodesBatchCommand( NodeCollection startNodes, RelationshipDescription relationshipDescription,
                                    NodeIdCollector targetNodeIdCollector, int batchSize )
    {
        this.startNodes = startNodes;
        this.relationshipDescription = relationshipDescription;
        this.targetNodeIdCollector = targetNodeIdCollector;
        this.batchSize = batchSize;
    }

    @Override
    public int numberOfIterations()
    {
        return startNodes.nodeIds().size();
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( GraphDatabaseService db, int index, Random random )
    {
        Node firstNode = db.getNodeById( startNodes.nodeIds().get( index ) );
        execute( firstNode, db, index, random );
    }

    @Override
    public void execute( Node currentNode, GraphDatabaseService db, int index, Random random )
    {
        totalRels += relationshipDescription
                .addRelationshipsToCurrentNode( db, currentNode, targetNodeIdCollector, random );
    }

    @Override
    public String description()
    {
        return String.format( "Creating '%s' relationships.", shortDescription() );
    }

    @Override
    public String shortDescription()
    {
        return relationshipDescription.createRelationshipDescription( startNodes.entityName() );
    }

    @Override
    public void onBegin( Log log )
    {
        log.write( String.format( "      %s", relationshipDescription.createRelationshipConstraintsDescription() ) );
    }

    @Override
    public void onEnd( Log log )
    {
        log.write( String.format( "      [Avg: %s relationship(s) per %s]",
                totalRels / startNodes.nodeIds().size(), startNodes.entityName() ) );
    }

    @Override
    public NodeCollection results()
    {
        return relationshipDescription.newDomainEntityInfo( targetNodeIdCollector.nodeIds() );
    }
}
