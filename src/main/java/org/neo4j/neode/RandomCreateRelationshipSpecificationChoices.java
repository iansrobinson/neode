package org.neo4j.neode;

import static org.neo4j.neode.probabilities.ProbabilityDistribution.flatDistribution;

import java.util.List;

class RandomCreateRelationshipSpecificationChoices extends CreateRelationshipSpecificationChoices
{
    protected RandomCreateRelationshipSpecificationChoices( List<CreateRelationshipSpecification>
                                                                    createRelationshipSpecifications )
    {
        super( createRelationshipSpecifications );
    }

    @Override
    protected Commands doCreateCommandSelector( List<BatchCommand<NodeCollection>> commands )
    {
        return new Commands( commands, new RandomCommandSelectionStrategy( flatDistribution() ) );
    }
}
