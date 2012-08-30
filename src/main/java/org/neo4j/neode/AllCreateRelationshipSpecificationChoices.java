package org.neo4j.neode;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;

class AllCreateRelationshipSpecificationChoices extends CreateRelationshipSpecificationChoices implements
        CommandSelectionStrategy
{
    public AllCreateRelationshipSpecificationChoices( List<CreateRelationshipSpecification>
                                                              createRelationshipSpecifications )
    {
        super(createRelationshipSpecifications);
    }

    @Override
    protected Commands doCreateCommandSelector( List<BatchCommand<NodeCollection>> commands )
    {
        return new Commands( commands, this );
    }

    @Override
    public BatchCommand<NodeCollection> nextCommand( List<BatchCommand<NodeCollection>> commands, Node currentNode,
                                                     Random random )
    {
        return new ExecuteAllCommandsBatchCommand( commands );
    }
}
