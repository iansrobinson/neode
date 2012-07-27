package org.neo4j.neode.commands;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.Distribution;
import org.neo4j.neode.numbergenerators.Range;

public class RandomCommandSelectionStrategy implements CommandSelectionStrategy
{
    private final Distribution distribution;

    public RandomCommandSelectionStrategy( Distribution distribution )
    {
        this.distribution = distribution;
    }

    @Override
    public BatchCommand<DomainEntityInfo> nextCommand( Node currentNode, List<BatchCommand<DomainEntityInfo>> commands,
                                                       Random random )
    {
        int index = distribution.generateSingle( Range.minMax(0, commands.size() - 1), random );
        return commands.get( index );
    }
}
