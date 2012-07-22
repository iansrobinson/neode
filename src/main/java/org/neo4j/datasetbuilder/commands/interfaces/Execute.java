package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.DomainEntityInfo;
import org.neo4j.datasetbuilder.Run;

public interface Execute
{
    DomainEntityInfo execute( Run run, int batchSize );

    DomainEntityInfo execute( Run run );
}
