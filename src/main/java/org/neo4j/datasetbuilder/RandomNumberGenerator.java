/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package org.neo4j.datasetbuilder;

import java.util.List;

public interface RandomNumberGenerator
{
    List<Integer> generate( int minNumberOfResults, int maxNumberOfResults, int min, int max );
    List<Integer> generate( int numberOfResults, int min, int max );
    int generateSingle(int min, int max);
}
