package org.neo4j.datasetbuilder.commandinterfaces;

import org.neo4j.datasetbuilder.BatchCommandExecutor;

public interface Execute
{
    void execute( BatchCommandExecutor executor );
}
