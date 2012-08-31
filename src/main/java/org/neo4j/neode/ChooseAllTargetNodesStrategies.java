package org.neo4j.neode;

import java.util.List;

class ChooseAllTargetNodesStrategies extends ChoiceOfTargetNodesStrategy implements CommandSelectionStrategy
{
    public ChooseAllTargetNodesStrategies( List<TargetNodesStrategy> targetNodesStrategies )
    {
        super( targetNodesStrategies );
    }

    @Override
    protected Commands doCreateCommandSelector( List<BatchCommand<NodeCollection>> commands )
    {
        return new Commands( commands, this );
    }

    @Override
    public BatchCommand<NodeCollection> nextCommand( List<BatchCommand<NodeCollection>> commands )
    {
        return new ExecuteAllCommandsBatchCommand( commands );
    }
}
