package org.neo4j.datasetbuilder.commands.interfaces;

import org.neo4j.datasetbuilder.BatchCommandExecutor;

public interface Execute
{
    void execute( BatchCommandExecutor executor, int batchSize );

    void execute( BatchCommandExecutor executor );
}
