package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.commands.Range;
import org.neo4j.datasetbuilder.commands.UniquenessStrategy;

public interface Cardinality
{
    Execute cardinality( Range value );
    Execute cardinality( Range value, UniquenessStrategy uniqueness );
}
