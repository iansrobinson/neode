/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.neode.properties;

import java.util.Random;

import org.neo4j.graphdb.PropertyContainer;

public interface PropertyValueGenerator<T>
{
    T generate(PropertyContainer propertyContainer, String propertyName, String nodeLabel, int iteration,
               Random random);
}
