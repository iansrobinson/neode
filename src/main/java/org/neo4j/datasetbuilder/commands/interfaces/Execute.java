package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.BatchCommandExecutor;
import org.neo4j.datasetbuilder.DomainEntityInfo;

public interface Execute
{
    DomainEntityInfo execute( BatchCommandExecutor executor, int batchSize );

    DomainEntityInfo execute( BatchCommandExecutor executor );
}
