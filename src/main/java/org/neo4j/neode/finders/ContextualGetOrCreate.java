package org.neo4j.neode.finders;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.neode.DomainEntity;

class ContextualGetOrCreate extends NodeFinder
{
    private final DomainEntity domainEntity;
    private final GraphQuery graphQuery;
    private final ExistingNodesFinder existingNodesFinder;

    ContextualGetOrCreate( DomainEntity domainEntity, GraphQuery graphQuery, ExistingNodesFinder existingNodesFinder )
    {
        this.domainEntity = domainEntity;
        this.graphQuery = graphQuery;
        this.existingNodesFinder = existingNodesFinder;
    }

    @Override
    public Iterable<Node> getNodes( GraphDatabaseService db, Node currentNode, int numberOfNodes, Random random )
    {
        List<Node> returnNodes = existingNodesFinder.getExistingNodes( graphQuery, currentNode, numberOfNodes, random );

        for ( int currentNodeIndex = 0; currentNodeIndex < returnNodes.size(); currentNodeIndex++ )
        {
            if ( returnNodes.get( currentNodeIndex ) == null )
            {
                Long nodeId = domainEntity.build( db, currentNodeIndex );
                returnNodes.set( currentNodeIndex, db.getNodeById( nodeId ) );
            }
        }

        return returnNodes;
    }

    @Override
    public String entityName()
    {
        return domainEntity.entityName();
    }
}
