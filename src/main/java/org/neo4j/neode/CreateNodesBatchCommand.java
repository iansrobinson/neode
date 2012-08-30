package org.neo4j.neode;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.logging.Log;

class CreateNodesBatchCommand implements BatchCommand<NodeCollection>
{
    private final NodeSpecification nodeSpecification;
    private final int numberOfIterations;
    private final NodeCollectionNew nodeCollection;
    private final int batchSize;

    CreateNodesBatchCommand( NodeSpecification nodeSpecification, int numberOfIterations, NodeCollectionNew nodeCollection, int batchSize )
    {
        this.nodeSpecification = nodeSpecification;
        this.numberOfIterations = numberOfIterations;
        this.nodeCollection = nodeCollection;
        this.batchSize = batchSize;
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
        nodeCollection.add( nodeSpecification.build( iteration ) );
    }

    @Override
    public void execute( Node currentNode, GraphDatabaseService db, int iteration, Random random )
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
        return nodeCollection.toNodeCollection();
    }
}
