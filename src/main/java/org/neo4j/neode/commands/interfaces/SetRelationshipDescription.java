package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.EntityChoices;
import org.neo4j.neode.commands.RelateToChoiceOfNodesBatchCommandBuilder;
import org.neo4j.neode.commands.RelationshipDescription;

public interface SetRelationshipDescription
{
    UpdateDataset to( RelationshipDescription entities );
    RelateToChoiceOfNodesBatchCommandBuilder to( EntityChoices entityChoices );
}
