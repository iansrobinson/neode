package org.neo4j.neode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class UniqueNodeIdCollector implements NodeIdCollector
{
    private final Set<Long> ids;

    UniqueNodeIdCollector()
    {
        ids = new HashSet<Long>();
    }

    UniqueNodeIdCollector( int size )
    {
        ids = new HashSet<Long>( size );
    }

    @Override
    public void add( Long nodeId )
    {
        ids.add( nodeId );
    }

    @Override
    public List<Long> nodeIds()
    {
        ArrayList<Long> idList = new ArrayList<Long>( ids );
        Collections.sort( idList );
        return idList;
    }

    @Override
    public List<Long> newList( int capacity )
    {
        return new ArrayList<Long>( capacity );
    }
}
