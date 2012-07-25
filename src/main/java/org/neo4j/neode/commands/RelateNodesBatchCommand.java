package org.neo4j.neode.commands;

import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.finders.NodeFinder;
import org.neo4j.neode.logging.Log;
import org.neo4j.neode.numbergenerators.Distribution;

class RelateNodesBatchCommand implements BatchCommand
{
    private final DomainEntityInfo startNodes;
    private final int batchSize;
    private final Range cardinality;
    private final Uniqueness uniqueness;
    private final NodeFinder nodeFinder;
    private final RelationshipType relationshipType;
    private final Direction direction;
    private final Distribution distribution;
    private final NodeIdCollector endNodeIdCollector;
    private long totalRels = 0;

    public RelateNodesBatchCommand( DomainEntityInfo startNodes, int batchSize,
                                    RelationshipType relationshipType,
                                    Direction direction, Range cardinality,
                                    Uniqueness uniqueness,
                                    NodeFinder nodeFinder, NodeIdCollector endNodeIdCollector )
    {
        this.startNodes = startNodes;
        this.batchSize = batchSize;
        this.relationshipType = relationshipType;
        this.direction = direction;
        this.cardinality = cardinality;
        this.uniqueness = uniqueness;
        this.nodeFinder = nodeFinder;
        this.endNodeIdCollector = endNodeIdCollector;

        distribution = Distribution.flatDistribution();
    }

    @Override
    public int numberOfIterations()
    {
        return startNodes.nodeIds().size();
    }

    @Override
    public int batchSize()
    {
        return batchSize;
    }

    @Override
    public void execute( GraphDatabaseService db, int index, Random random )
    {
        Node firstNode = db.getNodeById( startNodes.nodeIds().get( index ) );

        int numberOfRels = distribution.generateSingle( cardinality.min(), cardinality.max(), random );
        totalRels += numberOfRels;

        Iterable<Node> nodes = nodeFinder.getNodes( db, firstNode, numberOfRels, random );
        for ( Node secondNode : nodes )
        {
            endNodeIdCollector.add( secondNode.getId() );
            uniqueness.apply( db, firstNode, secondNode, relationshipType, direction );
        }
    }

    @Override
    public String description()
    {
        return String.format( "Creating '%s' relationships.", shortDescription() );
    }

    @Override
    public String shortDescription()
    {
        String relStart = "-";
        String relEnd = "->";
        if ( direction.equals( Direction.INCOMING ) )
        {
            relStart = "<-";
            relEnd = "-";
        }
        return String.format( "(%s)%s[:%s]%s(%s)", startNodes.entityName(), relStart,
                relationshipType.name(), relEnd, nodeFinder.entityName() );
    }

    @Override
    public void onBegin( Log log )
    {
        log.write( String.format( "      [Min: %s, Max: %s, Uniqueness: %s]", cardinality.min(), cardinality.max(),
                uniqueness.name() ) );
    }

    @Override
    public void onEnd( Log log )
    {
        log.write( String.format( "      [Avg: %s relationship(s) per %s]",
                totalRels / startNodes.nodeIds().size(), startNodes.entityName() ) );
    }

    @Override
    public DomainEntityInfo results()
    {
        return new DomainEntityInfo( nodeFinder.entityName(), endNodeIdCollector.nodeIds() );
    }
}
