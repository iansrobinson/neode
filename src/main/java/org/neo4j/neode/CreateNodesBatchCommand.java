package org.neo4j.neode;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.neode.logging.Log;

class CreateNodesBatchCommand implements BatchCommand<NodeIdCollection>
{
    private final NodeSpecification nodeSpecification;
    private final int numberOfIterations;
    private final NodeIdCollection nodeIdCollection;
    private final int batchSize;

    CreateNodesBatchCommand( NodeSpecification nodeSpecification, int numberOfIterations, NodeIdCollection
            nodeIdCollection, int batchSize )
    {
        this.nodeSpecification = nodeSpecification;
        this.numberOfIterations = numberOfIterations;
        this.nodeIdCollection = nodeIdCollection;
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
    public void execute( GraphDatabaseService db, int iteration )
    {
        nodeIdCollection.add( nodeSpecification.build( iteration ).getId() );
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
    public NodeIdCollection results()
    {
        return nodeIdCollection;
    }
}
