package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.EntityChoices;
import org.neo4j.neode.commands.RelateToChoiceOfNodesBatchCommandBuilder;
import org.neo4j.neode.commands.RelationshipSpecification;

public interface SetRelationshipDescription
{
    UpdateDataset to( RelationshipSpecification entities );
    RelateToChoiceOfNodesBatchCommandBuilder to( EntityChoices entityChoices );
}
