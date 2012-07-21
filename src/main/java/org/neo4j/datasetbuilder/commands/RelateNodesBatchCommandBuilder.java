package org.neo4j.datasetbuilder.commands;

import static org.neo4j.datasetbuilder.numbergenerators.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.neo4j.datasetbuilder.BatchCommand;
import org.neo4j.datasetbuilder.BatchCommandExecutor;
import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.logging.Log;
import org.neo4j.datasetbuilder.commands.interfaces.Execute;
import org.neo4j.datasetbuilder.commands.interfaces.NumberOfRels;
import org.neo4j.datasetbuilder.commands.interfaces.RelationshipName;
import org.neo4j.datasetbuilder.commands.interfaces.To;
import org.neo4j.datasetbuilder.finders.NodeFinderStrategy;
import org.neo4j.datasetbuilder.numbergenerators.NumberGenerator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

public class RelateNodesBatchCommandBuilder implements To, RelationshipName, NumberOfRels, Execute
{
    public static To relateEntities( DomainEntityInfo domainEntityInfo )
    {
        return new RelateNodesBatchCommandBuilder( domainEntityInfo );
    }

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private DomainEntityInfo domainEntityInfo;
    private Range range;
    private NodeFinderStrategy nodeFinderStrategy;
    private RelationshipType relationshipType;
    private Direction direction;

    private RelateNodesBatchCommandBuilder( DomainEntityInfo domainEntityInfo )
    {
        this.domainEntityInfo = domainEntityInfo;
    }

    @Override
    public RelationshipName to( NodeFinderStrategy nodeFinderStrategy )
    {
        this.nodeFinderStrategy = nodeFinderStrategy;
        return this;
    }

    @Override
    public Execute numberOfRels( Range value )
    {
        range = value;
        return this;
    }

    @Override
    public NumberOfRels relationship( RelationshipType value )
    {
        relationshipType = value;
        direction = Direction.OUTGOING;
        return this;
    }

    @Override
    public NumberOfRels relationship( RelationshipType value, Direction direction )
    {
        relationshipType = value;
        this.direction = direction;
        return this;
    }

    @Override
    public void execute( BatchCommandExecutor executor, int batchSize )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( domainEntityInfo, batchSize,
                relationshipType, direction, range, nodeFinderStrategy );
        executor.execute( command );
    }

    @Override
    public void execute( BatchCommandExecutor executor )
    {
        execute( executor, DEFAULT_BATCH_SIZE );
    }


    private class RelateNodesBatchCommand implements BatchCommand
    {
        private final DomainEntityInfo startNodeDomainEntityInfo;
        private final int batchSize;
        private final Range range;
        private final NodeFinderStrategy nodeFinderStrategy;
        private final RelationshipType relationshipType;
        private final Direction direction;
        private final NumberGenerator numberOfRelsGenerator;
        private long totalRels = 0;
        private Set<Long> endNodeIds = new HashSet<Long>();

        public RelateNodesBatchCommand( DomainEntityInfo startNodeDomainEntityInfo, int batchSize,
                                        RelationshipType relationshipType,
                                        Direction direction, Range range, NodeFinderStrategy nodeFinderStrategy )
        {
            this.startNodeDomainEntityInfo = startNodeDomainEntityInfo;
            this.batchSize = batchSize;
            this.relationshipType = relationshipType;
            this.direction = direction;
            this.range = range;
            this.nodeFinderStrategy = nodeFinderStrategy;

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
            Node startNode = db.getNodeById( startNodeDomainEntityInfo.nodeIds().get( index ) );

            int numberOfRels = numberOfRelsGenerator.generateSingle( range.min(), range.max(), random );
            totalRels += numberOfRels;

            Iterable<Node> nodes = nodeFinderStrategy.getNodes( db, startNode, numberOfRels, random );
            for ( Node endNode : nodes )
            {
                endNodeIds.add( endNode.getId() );
                if ( direction.equals( Direction.OUTGOING ) )
                {
                    startNode.createRelationshipTo( endNode, relationshipType );
                }
                else
                {
                    endNode.createRelationshipTo( startNode, relationshipType );
                }
            }
        }

        @Override
        public String description()
        {
            return String.format( "Creating '(%s)-[:%s]->(%s)' relationships.", startNodeDomainEntityInfo.entityName(),
                    relationshipType.name(), nodeFinderStrategy.entityName() );
        }

        @Override
        public void onBegin( Log log )
        {
            log.write( String.format( "      [Min: %s, Max: %s]", range.min(), range.max() ) );
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
            return new DomainEntityInfo( nodeFinderStrategy.entityName(), new ArrayList<Long>( endNodeIds ) );
        }
    }
}