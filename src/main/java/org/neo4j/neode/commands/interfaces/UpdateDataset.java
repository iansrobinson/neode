package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.commands.Dataset;

public interface UpdateDataset<T>
{
    T update( Dataset dataset, int batchSize );

    T update( Dataset dataset );

    void updateNoReturn( Dataset dataset, int batchSize );

    void updateNoReturn( Dataset dataset );
}
