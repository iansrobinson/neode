/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.RelationshipDescription;
import org.neo4j.neode.commands.RelationshipUniqueness;
import org.neo4j.neode.numbergenerators.Range;

public interface SetRelationshipConstraints
{
    RelationshipDescription relationshipConstraints( Range cardinality, RelationshipUniqueness relationshipUniqueness );

    RelationshipDescription relationshipConstraints( Range cardinality );
}
