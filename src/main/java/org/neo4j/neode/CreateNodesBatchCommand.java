package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.Log;

class CreateNodesBatchCommand implements BatchCommand<NodeCollection>
{
    private final NodeSpecification nodeSpecification;
    private final int numberOfIterations;
    private final int batchSize;
    private final NodeIdCollector nodeIdCollector;

    CreateNodesBatchCommand( NodeSpecification nodeSpecification, int numberOfIterations, int batchSize,
                             NodeIdCollector nodeIdCollector )
    {
        this.nodeSpecification = nodeSpecification;
        this.numberOfIterations = numberOfIterations;
        this.batchSize = batchSize;
        this.nodeIdCollector = nodeIdCollector;
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
    public void execute( GraphDatabaseService db, int iteration, Random random )
    {
        Long nodeId = nodeSpecification.build( db, iteration, random ).getId();
        nodeIdCollector.add( nodeId );
    }

    @Override
    public void execute( Node firstNode, GraphDatabaseService db, int iteration, Random random )
    {
        execute( db, iteration, random );
    }

    @Override
    public String description()
    {
        return "Creating '" + shortDescription() + "' nodes.";
    }

    @Override
    public String shortDescription()
    {
        return nodeSpecification.label();
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
    public NodeCollection results()
    {
        return new NodeCollection( nodeSpecification.label(), nodeIdCollector.nodeIds() );
    }
}
