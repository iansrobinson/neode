package org.neo4j.datasetbuilder;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.neo4j.datasetbuilder.commandinterfaces.BatchSize;
import org.neo4j.datasetbuilder.commandinterfaces.EndNodeName;
import org.neo4j.datasetbuilder.commandinterfaces.Execute;
import org.neo4j.datasetbuilder.commandinterfaces.FindEndNodes;
import org.neo4j.datasetbuilder.commandinterfaces.MinMaxNumberOfRels;
import org.neo4j.datasetbuilder.commandinterfaces.RelationshipName;
import org.neo4j.datasetbuilder.commandinterfaces.StartNodeName;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class RelateNodesBatchCommandBuilder implements StartNodeName, EndNodeName, RelationshipName,
        BatchSize, MinMaxNumberOfRels,
        FindEndNodes, Execute
{
    public static StartNodeName relateNodes( List<Long> startNodeIds )
    {
        return new RelateNodesBatchCommandBuilder( startNodeIds );
    }

    private List<Long> startNodeIds = new ArrayList<Long>();
    private int batchSize = 2000;
    private String startNodeName = "UNKNOWN";
    private String endNodeName = "UNKNOWN";
    private int minRelsPerNode = 1;
    private int maxRelsPerNode = 1;
    private NodeFinder nodeFinder = new NodeFinder()
    {

        @Override
        public Iterable<Node> getNodes( GraphDatabaseService db, int numberOfNodes )
        {
            return emptyList();
        }
    };
    private String relationshipName = "UNKNOWN";

    private RelateNodesBatchCommandBuilder( List<Long> startNodeIds )
    {
        this.startNodeIds = startNodeIds;
    }

    @Override
    public EndNodeName startNodeName( String value )
    {
        startNodeName = value;
        return this;
    }

    @Override
    public RelationshipName endNodeName( String value )
    {
        endNodeName = value;
        return this;
    }

    @Override
    public BatchSize relationshipName( String value )
    {
        relationshipName = value;
        return this;
    }

    @Override
    public MinMaxNumberOfRels batchSize( int value )
    {
        batchSize = value;
        return this;
    }

    @Override
    public FindEndNodes minMaxNumberOfRels( int min, int max )
    {
        minRelsPerNode = min;
        maxRelsPerNode = max;
        return this;
    }

    @Override
    public Execute findEndNodesUsing( NodeFinder finder )
    {
        nodeFinder = finder;
        return this;
    }

    @Override
    public void execute( BatchCommandExecutor executor )
    {
        RelateNodesBatchCommand command = new RelateNodesBatchCommand( startNodeIds, batchSize,
                relationshipName, startNodeName, endNodeName, minRelsPerNode, maxRelsPerNode, nodeFinder );
        executor.execute( command );
    }

    private class RelateNodesBatchCommand implements BatchCommand<List<Long>>
    {
        private final List<Long> startNodeIds;
        private final int batchSize;
        private final String startNodeName;
        private final String endNodeName;
        private final int minRelsPerNode;
        private final int maxRelsPerNode;
        private final NodeFinder nodeFinder;
        private final DynamicRelationshipType relationshipType;
        private final RandomNumberGenerator generator;

        private long totalRels = 0;
        private Set<Long> endNodeIds = new HashSet<Long>();

        public RelateNodesBatchCommand( List<Long> startNodeIds, int batchSize, String relationshipName,
                                        String startNodeName,
                                        String endNodeName, int minRelsPerNode, int maxRelsPerNode,
                                        NodeFinder nodeFinder )
        {
            this.startNodeIds = startNodeIds;
            this.batchSize = batchSize;
            this.startNodeName = startNodeName;
            this.endNodeName = endNodeName;
            this.minRelsPerNode = minRelsPerNode;
            this.maxRelsPerNode = maxRelsPerNode;
            this.nodeFinder = nodeFinder;
            relationshipType = DynamicRelationshipType.withName( relationshipName );
            generator = new UniqueRandNumberGenerator( new Random() );
        }


        @Override
        public int numberOfIterations()
        {
            return startNodeIds.size();
        }

        @Override
        public int batchSize()
        {
            return batchSize;
        }

        @Override
        public void execute( GraphDatabaseService db, int index )
        {
            Node startNode = db.getNodeById( startNodeIds.get( index ) );

            int numberOfRels = generator.generateSingle( minRelsPerNode, maxRelsPerNode );
            totalRels += numberOfRels;

            Iterable<Node> nodes = nodeFinder.getNodes( db, numberOfRels );
            for ( Node endNode : nodes )
            {
                endNodeIds.add( endNode.getId() );
                startNode.createRelationshipTo( endNode, relationshipType );
            }
        }

        @Override
        public String description()
        {
            return String.format( "Creating relationships: (%s)-[:%s]->(%s).",
                    startNodeName, relationshipType.name(), endNodeName );
        }

        @Override
        public void onBegin( Log log )
        {
            log.write( String.format( "      [Min: %s, Max: %s]", minRelsPerNode, maxRelsPerNode ) );
        }

        @Override
        public void onEnd( Log log )
        {
            log.write( String.format( "      [Avg: %s relationship(s) per %s]", totalRels / startNodeIds.size(),
                    startNodeName ) );
        }

        @Override
        public List<Long> value()
        {
            return new ArrayList<Long>( endNodeIds );
        }
    }
}