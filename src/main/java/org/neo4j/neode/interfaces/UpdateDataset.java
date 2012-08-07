package org.neo4j.neode.interfaces;

import org.neo4j.neode.Dataset;

public interface UpdateDataset<T>
{
    T update( Dataset dataset, int batchSize );

    T update( Dataset dataset );

    void updateNoReturn( Dataset dataset, int batchSize );

    void updateNoReturn( Dataset dataset );
}
