package org.neo4j.neode.commands;

import static org.neo4j.neode.numbergenerators.Range.minMax;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;

class ExistingUniqueNodeFinder extends NodeFinder
{
    private final DomainEntityInfo domainEntityInfo;
    private final ProbabilityDistribution probabilityDistribution;

    ExistingUniqueNodeFinder( DomainEntityInfo domainEntityInfo, ProbabilityDistribution probabilityDistribution )
    {
        this.domainEntityInfo = domainEntityInfo;
        this.probabilityDistribution = probabilityDistribution;
    }

    @Override
    public Iterable<Node> getNodes( int quantity, final GraphDatabaseService db, Node currentNode, Random random )
    {
        final List<Integer> indexes;
        try
        {
            indexes = probabilityDistribution.generateList(
                    quantity,
                    minMax( 0, domainEntityInfo.nodeIds().size() - 1 ),
                    random );
        }
        catch ( IllegalArgumentException e )
        {
            throw new IllegalStateException( String.format( "Get existing '%s' nodes command cannot complete " +
                    "because the maximum number of nodes available is smaller than the number of " +
                    "nodes specified when applying the relationship constraint. Number of nodes specified by "  +
                    "relationship constraint: %s. Maximum number of nodes available: %s. Either adjust the " +
                    "relationship constraint or increase the number of nodes available.",
                    domainEntityInfo.entityName(), quantity, domainEntityInfo.nodeIds().size() ) );
        }
        return new Iterable<Node>()
        {
            @Override
            public Iterator<Node> iterator()
            {
                return new NodeIterator( domainEntityInfo.nodeIds(), indexes, db );
            }
        };
    }

    @Override
    public String entityName()
    {
        return domainEntityInfo.entityName();
    }

}
