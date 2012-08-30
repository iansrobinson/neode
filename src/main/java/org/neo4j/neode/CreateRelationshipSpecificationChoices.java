package org.neo4j.neode;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public abstract class CreateRelationshipSpecificationChoices
{
    public static CreateRelationshipSpecificationChoices randomChoice( CreateRelationshipSpecification... createRelationshipSpecifications )
    {
        return new RandomCreateRelationshipSpecificationChoices( asList( createRelationshipSpecifications ) );
    }

    public static CreateRelationshipSpecificationChoices all( CreateRelationshipSpecification... createRelationshipSpecifications )
        {
            return new AllCreateRelationshipSpecificationChoices( asList( createRelationshipSpecifications ) );
        }

    private final List<CreateRelationshipSpecification> createRelationshipSpecifications;

    protected CreateRelationshipSpecificationChoices( List<CreateRelationshipSpecification>
                                                              createRelationshipSpecifications )
    {
        this.createRelationshipSpecifications = createRelationshipSpecifications;
    }

    Commands createCommandSelector( NodeCollection startNodes, int batchSize,
                                           NodeIdCollectorFactory nodeIdCollectorFactory )
    {
        List<BatchCommand<NodeCollection>> commands = new ArrayList<BatchCommand<NodeCollection>>();
        for ( CreateRelationshipSpecification createRelationshipSpecification : createRelationshipSpecifications )
        {
            RelateNodesBatchCommand command = new RelateNodesBatchCommand(
                    startNodes, createRelationshipSpecification, nodeIdCollectorFactory.createNodeIdCollector(), batchSize );
            commands.add( command );
        }
        return doCreateCommandSelector( commands );
    }

    abstract protected Commands doCreateCommandSelector( List<BatchCommand<NodeCollection>> commands );
}
