package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.commands.Dataset;

public interface UpdateDataset
{
    DomainEntityInfo update( Dataset dataset, int batchSize );

    DomainEntityInfo update( Dataset dataset );

    void updateNoReturn( Dataset dataset, int batchSize );

    void updateNoReturn( Dataset dataset );
}
