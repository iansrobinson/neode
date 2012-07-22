/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder;

import org.neo4j.graphdb.Node;

public interface PropertySetterStrategy
{
    void setProperty(Node node, String propertyName, String entityName, int index);
}
