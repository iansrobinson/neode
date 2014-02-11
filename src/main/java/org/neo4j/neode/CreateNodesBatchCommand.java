package org.neo4j.neode;

import org.neo4j.neode.logging.Log;

class CreateNodesBatchCommand implements BatchCommand<NodeCollection>
{
    private final NodeSpecification nodeSpecification;
    private final int numberOfIterations;
    private final NodeCollection nodeCollection;
    private final int batchSize;

    CreateNodesBatchCommand( NodeSpecification nodeSpecification, int numberOfIterations, NodeCollection
            nodeCollection, int batchSize )
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
    public void execute( int iteration )
    {
        nodeCollection.add( nodeSpecification.build( iteration ) );
    }

    @Override
    public String description()
    {
        return "Creating '" + shortDescription() + "' nodes.";
    }

    @Override
    public String shortDescription()
    {
        return nodeSpecification.labelName();
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
        return nodeCollection;
    }
}
