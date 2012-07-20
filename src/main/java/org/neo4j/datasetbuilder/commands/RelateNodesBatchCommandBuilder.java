package org.neo4j.datasetbuilder.commands;

import static java.util.Collections.emptyList;
import static org.neo4j.datasetbuilder.randomnumbers.FlatDistributionUniqueRandomNumberGenerator.flatDistribution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.neo4j.datasetbuilder.BatchCommand;
import org.neo4j.datasetbuilder.BatchCommandExecutor;
import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.Log;
import org.neo4j.datasetbuilder.commands.interfaces.BatchSize;
import org.neo4j.datasetbuilder.commands.interfaces.Execute;
import org.neo4j.datasetbuilder.commands.interfaces.MinMaxNumberOfRels;
import org.neo4j.datasetbuilder.commands.interfaces.RelationshipName;
import org.neo4j.datasetbuilder.commands.interfaces.To;
import org.neo4j.datasetbuilder.finders.NodeFinderStrategy;
import org.neo4j.datasetbuilder.randomnumbers.RandomNumberGenerator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

public class RelateNodesBatchCommandBuilder implements To, RelationshipName,
        BatchSize, MinMaxNumberOfRels, Execute
{
    public static To relateEntities( DomainEntityInfo domainEntityInfo )
    {
        return new RelateNodesBatchCommandBuilder( domainEntityInfo );
    }

    private DomainEntityInfo domainEntityInfo;
    private DomainEntityBuilder endNodeDomainEntityBuilder;
    private int batchSize = 2000;
    private int minRelsPerNode = 1;
    private int maxRelsPerNode = 1;
    private NodeFinderStrategy nodeFinderStrategyStrategy = new NodeFinderStrategy()
    {

        @Override
        public Iterable<Node> getNodes( GraphDatabaseService db, int numberOfNodes, DomainEntityBuilder
                domainEntityBuilder, Random random )
        {
            return emptyList();
        }
    };
    private RelationshipType relationshipType = null;

    private RelateNodesBatchCommandBuilder( DomainEntityInfo domainEntityInfo )
    {
        this.domainEntityInfo = domainEntityInfo;
    }

    @Override
    public RelationshipName to( DomainEntityBuilder domainEntity, NodeFinderStrategy nodeStrategyStrategy )
    {
        endNodeDomainEntityBuilder = domainEntity;
        nodeFinderStrategyStrategy = nodeStrategyStrategy;
        return this;
    }



    @Override
    public MinMaxNumberOfRels batchSize( int value )
    {
        batchSize = value;
        return this;
    }

    @Override
    public Execute minMaxNumberOfRels( int min, int max )
    {
        minRelsPerNode = min;
        maxRelsPerNode = max;
        return this;
    }

    @Override
    public void execute( BatchCommandExecutor executor )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( domainEntityInfo, endNodeDomainEntityBuilder,
                batchSize, relationshipType, minRelsPerNode, maxRelsPerNode, nodeFinderStrategyStrategy );
        executor.execute( command );
    }

    @Override
    public BatchSize relationship( RelationshipType value )
    {
        relationshipType = value;
        return this;
    }

    private class RelateNodesBatchCommand implements BatchCommand
    {
        private final DomainEntityInfo startNodeDomainEntityInfo;
        private final DomainEntityBuilder endNodeDomainEntityBuilder;

        private final int batchSize;
        private final int minRelsPerNode;
        private final int maxRelsPerNode;
        private final NodeFinderStrategy nodeFinderStrategy;
        private final RelationshipType relationshipType;
        private final RandomNumberGenerator numberOfRelsGenerator;

        private long totalRels = 0;
        private Set<Long> endNodeIds = new HashSet<Long>();

        public RelateNodesBatchCommand( DomainEntityInfo startNodeDomainEntityInfo, DomainEntityBuilder
                endNodeDomainEntityBuilder, int batchSize, RelationshipType relationshipType,
                                        int minRelsPerNode, int maxRelsPerNode,
                                        NodeFinderStrategy nodeFinderStrategy )
        {
            this.startNodeDomainEntityInfo = startNodeDomainEntityInfo;
            this.endNodeDomainEntityBuilder = endNodeDomainEntityBuilder;
            this.batchSize = batchSize;
            this.relationshipType = relationshipType;
            this.minRelsPerNode = minRelsPerNode;
            this.maxRelsPerNode = maxRelsPerNode;
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

            int numberOfRels = numberOfRelsGenerator.generateSingle( minRelsPerNode, maxRelsPerNode, random );
            totalRels += numberOfRels;

            Iterable<Node> nodes = nodeFinderStrategy.getNodes( db, numberOfRels, endNodeDomainEntityBuilder, random );
            for ( Node endNode : nodes )
            {
                endNodeIds.add( endNode.getId() );
                startNode.createRelationshipTo( endNode, relationshipType );
            }
        }

        @Override
        public String description()
        {
            return String.format( "Creating relationships: (%s)-[:%s]->(%s).", startNodeDomainEntityInfo.entityName(),
                    relationshipType.name(), endNodeDomainEntityBuilder.entityName() );
        }

        @Override
        public void onBegin( Log log )
        {
            log.write( String.format( "      [Min: %s, Max: %s]", minRelsPerNode, maxRelsPerNode ) );
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
            return new DomainEntityInfo( endNodeDomainEntityBuilder.entityName(), new ArrayList<Long>( endNodeIds ) );
        }
    }
}