package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.NodeCollection;
import org.neo4j.neode.commands.Dataset;

public interface UpdateDataset
{
    NodeCollection update( Dataset dataset, int batchSize );

    NodeCollection update( Dataset dataset );

    void updateNoReturn( Dataset dataset, int batchSize );

    void updateNoReturn( Dataset dataset );
}
