package org.neo4j.neode.commands;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.logging.Log;

class DomainEntityBatchCommand implements BatchCommand<DomainEntityInfo>
{
    private final DomainEntity domainEntity;
    private final int numberOfIterations;
    private final int batchSize;
    private final NodeIdCollector endNodeIdCollector;

    public DomainEntityBatchCommand( DomainEntity domainEntity, int numberOfIterations,
                                     int batchSize, NodeIdCollector endNodeIdCollector )
    {
        this.domainEntity = domainEntity;
        this.numberOfIterations = numberOfIterations;
        this.batchSize = batchSize;
        this.endNodeIdCollector = endNodeIdCollector;
    }

    @Override
    public int numberOfIterations()
    {
        return numberOfIterations;
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( GraphDatabaseService db, int index, Random random )
    {
        Long nodeId = domainEntity.build( db, index, random );
        endNodeIdCollector.add( nodeId );
    }

    @Override
    public void execute( GraphDatabaseService db, Node firstNode, int index, Random random )
    {
        execute( db, index, random );
    }

    @Override
    public String description()
    {
        return "Creating '" + shortDescription() + "' nodes.";
    }

    @Override
    public String shortDescription()
    {
        return domainEntity.entityName();
    }

    @Override
    public void onBegin( Log log )
    {
        // Do nothing
    }

    @Override
    public void onEnd( Log log )
    {
        // Do nothing
    }

    @Override
    public DomainEntityInfo results()
    {
        return new DomainEntityInfo( domainEntity.entityName(), endNodeIdCollector.nodeIds() );
    }
}
