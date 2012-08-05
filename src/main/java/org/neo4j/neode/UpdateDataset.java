package org.neo4j.neode;

public interface UpdateDataset<T>
{
    T update( Dataset dataset, int batchSize );

    T update( Dataset dataset );

    void updateNoReturn( Dataset dataset, int batchSize );

    void updateNoReturn( Dataset dataset );
}
