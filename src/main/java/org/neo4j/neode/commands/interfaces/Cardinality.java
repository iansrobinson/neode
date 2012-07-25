package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.Range;
import org.neo4j.neode.commands.Uniqueness;

public interface Cardinality
{
    Update cardinality( Range value );

    Update cardinality( Range value, Uniqueness uniqueness );
}
