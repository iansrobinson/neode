package org.neo4j.datasetbuilder;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

public class DomainEntityBatchCommandBuilder
{
    public static DomainEntityBatchCommandBuilder createEntity( String entityName )
    {
        return new DomainEntityBatchCommandBuilder( entityName );
    }

    private String entityName = "";
    private int numberOfIterations = 0;
    private int batchSize = 10000;
    private String propertyName = "name";
    private boolean isIndexable = false;

    private DomainEntityBatchCommandBuilder( String entityName )
    {
        this.entityName = entityName;
    }

    public DomainEntityBatchCommandBuilder numberOfIterations( int value )
    {
        numberOfIterations = value;
        return this;
    }

    public DomainEntityBatchCommandBuilder batchSize( int value )
    {
        batchSize = value;
        return this;
    }

    public DomainEntityBatchCommandBuilder propertyName( String name )
    {
        propertyName = name;
        return this;
    }

    public DomainEntityBatchCommandBuilder isIndexable( boolean value )
    {
        isIndexable = value;
        return this;
    }

    public List<Long> execute( BatchCommandExecutor executor )
    {
        DomainEntityBatchCommand command = new DomainEntityBatchCommand(
                entityName, numberOfIterations, batchSize, propertyName, isIndexable );
        Results<List<Long>> results = executor.execute( command );
        return results.value();
    }

    private static class DomainEntityBatchCommand implements BatchCommand<List<Long>>
    {
        private final String entityName;
        private final int numberOfIterations;
        private final int batchSize;
        private final String propertyName;
        private final boolean isIndexable;
        private final List<Long> nodeIds;

        public DomainEntityBatchCommand( String entityName, int numberOfIterations, int batchSize, String propertyName,
                                         boolean isIndexable )
        {
            this.entityName = entityName;
            this.numberOfIterations = numberOfIterations;
            this.batchSize = batchSize;
            this.propertyName = propertyName;
            this.isIndexable = isIndexable;
            nodeIds = new ArrayList<Long>();
        }

        @Override
        public int numberOfIterations()
        {
            return numberOfIterations;
        }

        @Override
        public int batchSize()
        {
            return batchSize;
        }

        @Override
        public void execute( GraphDatabaseService db, int index )
        {
            Node node = db.createNode();
            String value = String.format( "%s-%s", entityName, index );
            node.setProperty( propertyName, value );
            if ( isIndexable )
            {
                db.index().forNodes( entityName ).add( node, propertyName, value );
            }
            nodeIds.add( node.getId() );
        }

        @Override
        public String description()
        {
            return "Creating '" + entityName + "' nodes.";
        }

        @Override
        public void onBegin( Log log )
        {
            // Do nothing
        }

        @Override
        public void onEnd( Log log )
        {
            // Do nothing
        }

        @Override
        public List<Long> value()
        {
            return nodeIds;
        }
    }
}
