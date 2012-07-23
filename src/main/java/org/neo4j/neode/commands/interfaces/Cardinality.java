package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.Range;
import org.neo4j.neode.commands.UniquenessStrategy;

public interface Cardinality
{
    AddTo cardinality( Range value );
    AddTo cardinality( Range value, UniquenessStrategy uniqueness );
}
