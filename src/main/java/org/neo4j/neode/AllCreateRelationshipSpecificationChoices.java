package org.neo4j.neode;

import java.util.List;

import org.neo4j.graphdb.Node;

class AllCreateRelationshipSpecificationChoices extends CreateRelationshipSpecificationChoices implements
        CommandSelectionStrategy
{
    public AllCreateRelationshipSpecificationChoices( List<TargetNodes>
                                                              targetNodeses )
    {
        super( targetNodeses );
    }

    @Override
    protected Commands doCreateCommandSelector( List<BatchCommand<NodeIdCollection>> commands )
    {
        return new Commands( commands, this );
    }

    @Override
    public BatchCommand<NodeIdCollection> nextCommand( List<BatchCommand<NodeIdCollection>> commands, Node currentNode )
    {
        return new ExecuteAllCommandsBatchCommand( commands );
    }
}
