package org.neo4j.neode.commands;

import static org.neo4j.neode.numbergenerators.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.commands.interfaces.Cardinality;
import org.neo4j.neode.commands.interfaces.RelationshipName;
import org.neo4j.neode.commands.interfaces.To;
import org.neo4j.neode.commands.interfaces.Update;
import org.neo4j.neode.finders.NodeFinder;
import org.neo4j.neode.logging.Log;
import org.neo4j.neode.numbergenerators.NumberGenerator;

public class RelateNodesBatchCommandBuilder implements To, RelationshipName, Cardinality, Update
{

    public static To relateEntities( DomainEntityInfo domainEntityInfo )
    {
        return new RelateNodesBatchCommandBuilder( domainEntityInfo );
    }

    private static final int DEFAULT_BATCH_SIZE = 10000;

    private DomainEntityInfo domainEntityInfo;
    private Range cardinality;
    private Uniqueness uniqueness;
    private NodeFinder nodeFinder;
    private RelationshipType relationshipType;
    private Direction direction;

    private RelateNodesBatchCommandBuilder( DomainEntityInfo domainEntityInfo )
    {
        this.domainEntityInfo = domainEntityInfo;
    }

    @Override
    public RelationshipName to( NodeFinder nodeFinder )
    {
        this.nodeFinder = nodeFinder;
        return this;
    }

    @Override
    public Update cardinality( Range value )
    {
        cardinality = value;
        uniqueness = Uniqueness.ALLOW_MULTIPLE;
        return this;
    }

    @Override
    public Update cardinality( Range value, Uniqueness uniqueness )
    {
        cardinality = value;
        this.uniqueness = uniqueness;
        return this;
    }

    @Override
    public Cardinality relationship( RelationshipType value )
    {
        relationshipType = value;
        direction = Direction.OUTGOING;
        return this;
    }

    @Override
    public Cardinality relationship( RelationshipType value, Direction direction )
    {
        relationshipType = value;
        this.direction = direction;
        return this;
    }

    @Override
    public DomainEntityInfo update( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( domainEntityInfo, batchSize,
                relationshipType, direction, cardinality, uniqueness, nodeFinder, true );
        return dataset.execute( command );
    }

    @Override
    public DomainEntityInfo update( Dataset dataset )
    {
        return update( dataset, DEFAULT_BATCH_SIZE );
    }

    @Override
    public void updateNoReturn( Dataset dataset, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( domainEntityInfo, batchSize,
                relationshipType, direction, cardinality, uniqueness, nodeFinder, false );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }


    private class RelateNodesBatchCommand implements BatchCommand
    {
        private final DomainEntityInfo startNodeDomainEntityInfo;
        private final int batchSize;
        private final Range cardinality;
        private final Uniqueness uniqueness;
        private final NodeFinder nodeFinder;
        private final RelationshipType relationshipType;
        private final Direction direction;
        private final NumberGenerator numberOfRelsGenerator;
        private final boolean captureEndNodeIds;
        private long totalRels = 0;
        private Set<Long> endNodeIds = new HashSet<Long>();

        public RelateNodesBatchCommand( DomainEntityInfo startNodeDomainEntityInfo, int batchSize,
                                        RelationshipType relationshipType,
                                        Direction direction, Range cardinality,
                                        Uniqueness uniqueness,
                                        NodeFinder nodeFinder, boolean captureEndNodeIds )
        {
            this.startNodeDomainEntityInfo = startNodeDomainEntityInfo;
            this.batchSize = batchSize;
            this.relationshipType = relationshipType;
            this.direction = direction;
            this.cardinality = cardinality;
            this.uniqueness = uniqueness;
            this.nodeFinder = nodeFinder;
            this.captureEndNodeIds = captureEndNodeIds;

            numberOfRelsGenerator = flatDistribution();
        }

        @Override
        public int numberOfIterations()
        {
            return startNodeDomainEntityInfo.nodeIds().size();
        }

        @Override
        public int batchSize()
        {
            return batchSize;
        }

        @Override
        public void execute( GraphDatabaseService db, int index, Random random )
        {
            Node firstNode = db.getNodeById( startNodeDomainEntityInfo.nodeIds().get( index ) );

            int numberOfRels = numberOfRelsGenerator.generateSingle( cardinality.min(), cardinality.max(), random );
            totalRels += numberOfRels;

            Iterable<Node> nodes = nodeFinder.getNodes( db, firstNode, numberOfRels, random );
            for ( Node secondNode : nodes )
            {
                if ( captureEndNodeIds )
                {
                    endNodeIds.add( secondNode.getId() );
                }
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
            return String.format( "(%s)%s[:%s]%s(%s)", startNodeDomainEntityInfo.entityName(), relStart,
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
                    totalRels / startNodeDomainEntityInfo.nodeIds().size(), startNodeDomainEntityInfo.entityName() ) );
        }

        @Override
        public DomainEntityInfo results()
        {
            return new DomainEntityInfo( nodeFinder.entityName(), new ArrayList<Long>( endNodeIds ) );
        }
    }
}