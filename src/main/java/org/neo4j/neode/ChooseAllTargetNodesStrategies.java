package org.neo4j.neode;

import java.util.List;

class ChooseAllTargetNodesStrategies extends ChoiceOfTargetNodesStrategy implements CommandSelectionStrategy
{
    public ChooseAllTargetNodesStrategies( List<TargetNodesStrategy> targetNodesStrategies )
    {
        super( targetNodesStrategies );
    }

    @Override
    protected Commands doCreateCommandSelector( List<BatchCommand<NodeIdCollection>> commands )
    {
        return new Commands( commands, this );
    }

    @Override
    public BatchCommand<NodeIdCollection> nextCommand( List<BatchCommand<NodeIdCollection>> commands )
    {
        return new ExecuteAllCommandsBatchCommand( commands );
    }
}
