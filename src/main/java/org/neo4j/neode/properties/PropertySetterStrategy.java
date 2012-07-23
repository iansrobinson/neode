/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.properties;

import org.neo4j.graphdb.Node;

public interface PropertySetterStrategy
{
    Object setProperty( Node node, String propertyName, String entityName, int index );
}
