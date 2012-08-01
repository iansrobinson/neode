/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.RelationshipSpecification;
import org.neo4j.neode.commands.RelationshipUniqueness;
import org.neo4j.neode.numbergenerators.Range;

public interface SetRelationshipConstraints
{
    RelationshipSpecification relationshipConstraints( Range cardinality, RelationshipUniqueness relationshipUniqueness );

    RelationshipSpecification relationshipConstraints( Range cardinality );
}
