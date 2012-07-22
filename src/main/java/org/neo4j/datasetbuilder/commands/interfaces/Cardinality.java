package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.commands.Range;
import org.neo4j.datasetbuilder.commands.UniquenessStrategy;

public interface Cardinality
{
    AddTo cardinality( Range value );
    AddTo cardinality( Range value, UniquenessStrategy uniqueness );
}
