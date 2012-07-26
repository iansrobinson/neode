/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.RelationshipDescription;
import org.neo4j.neode.commands.Uniqueness;
import org.neo4j.neode.numbergenerators.Range;

public interface CreateRelationshipConstraints
{
    RelationshipDescription relationshipConstraints( Range cardinality, Uniqueness uniqueness );

    RelationshipDescription relationshipConstraints( Range cardinality );
}
