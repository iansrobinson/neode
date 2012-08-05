package org.neo4j.neode;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;
import org.neo4j.neode.numbergenerators.Range;

class RandomCommandSelectionStrategy implements CommandSelectionStrategy
{
    private final ProbabilityDistribution probabilityDistribution;

    public RandomCommandSelectionStrategy( ProbabilityDistribution probabilityDistribution )
    {
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public BatchCommand<NodeCollection> nextCommand( List<BatchCommand<NodeCollection>> commands, Node currentNode,
                                                       Random random )
    {
        int index = probabilityDistribution.generateSingle( Range.minMax(0, commands.size() - 1), random );
        return commands.get( index );
    }
}
