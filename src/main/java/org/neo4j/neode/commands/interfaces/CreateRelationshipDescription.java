package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.RelationshipDescription;

public interface CreateRelationshipDescription
{
    UpdateDataset to( RelationshipDescription entities );
}
