package org.neo4j.neode.commands.interfaces;

import java.util.List;

import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.NodeChoices;
import org.neo4j.neode.commands.RelationshipSpecification;

public interface SetRelationshipDescription
{
    UpdateDataset<NodeCollection> to( RelationshipSpecification entities );
    UpdateDataset<List<NodeCollection>> to( NodeChoices nodeChoices );
}
