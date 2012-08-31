package org.neo4j.neode.probabilities;

import org.neo4j.neode.Range;

class FlatProbabilityDistributionUnique extends BaseUniqueProbabilityDistribution
{
    protected int getNextNumber( Range minMax )
    {
        return minMax.min() + random().nextInt( minMax.max() + 1 );
    }

    @Override
    public String description()
    {
        return "flat";
    }
}
