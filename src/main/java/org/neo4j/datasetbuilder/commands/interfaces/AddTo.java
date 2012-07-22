package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.Dataset;

public interface AddTo
{
    DomainEntityInfo addTo( Dataset dataset, int batchSize );

    DomainEntityInfo addTo( Dataset dataset );
}
