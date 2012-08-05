package org.neo4j.neode.commands;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.logging.Log;

class NodeBatchCommand implements BatchCommand<NodeCollection>
{
    private final NodeSpecification nodeSpecification;
    private final int numberOfIterations;
    private final int batchSize;
    private final NodeIdCollector endNodeIdCollector;

    public NodeBatchCommand( NodeSpecification nodeSpecification, int numberOfIterations,
                             int batchSize, NodeIdCollector endNodeIdCollector )
    {
        this.nodeSpecification = nodeSpecification;
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
    public void execute( GraphDatabaseService db, int iteration, Random random )
    {
        Long nodeId = nodeSpecification.build( iteration, random ).getId();
        endNodeIdCollector.add( nodeId );
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
        return new NodeCollection( nodeSpecification.label(), endNodeIdCollector.nodeIds() );
    }
}
