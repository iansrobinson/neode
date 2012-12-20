package org.neo4j.neode.interfaces;

import org.neo4j.neode.DatasetManager;

public interface InitialDatasetDefinition<PARAMETERS>
{
    void createDataset( DatasetManager datasetManager, PARAMETERS parameters );
}
