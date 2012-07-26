package org.neo4j.neode.commands;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.Dataset;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.commands.interfaces.Cardinality;
import org.neo4j.neode.commands.interfaces.RelationshipName;
import org.neo4j.neode.commands.interfaces.To;
import org.neo4j.neode.commands.interfaces.Update;
import org.neo4j.neode.finders.NodeFinder;
import org.neo4j.neode.numbergenerators.Range;

public class RelateNodesBatchCommandBuilder implements To, RelationshipName, Cardinality, Update
{
    private static final int DEFAULT_BATCH_SIZE = 10000;

    private DomainEntityInfo domainEntityInfo;
    private Range cardinality;
    private Uniqueness uniqueness;
    private NodeFinder nodeFinder;
    private RelationshipType relationshipType;
    private Direction direction;

    public RelateNodesBatchCommandBuilder( DomainEntityInfo domainEntityInfo )
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
                relationshipType, direction, cardinality, uniqueness, nodeFinder, new EndNodeIdCollector() );
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
                relationshipType, direction, cardinality, uniqueness, nodeFinder, NullEndNodeIdCollector.INSTANCE );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }


}