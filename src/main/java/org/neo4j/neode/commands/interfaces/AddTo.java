package org.neo4j.neode.commands.interfaces;

import org.neo4j.neode.Dataset;
import org.neo4j.neode.DomainEntityInfo;

public interface AddTo
{
    DomainEntityInfo addTo( Dataset dataset, int batchSize );

    DomainEntityInfo addTo( Dataset dataset );
}
