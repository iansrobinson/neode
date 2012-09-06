package org.neo4j.neode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

final class NullSet<T> implements Set<T>
{
    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public boolean contains( Object o )
    {
        return false;
    }

    @Override
    public Iterator<T> iterator()
    {
        return null;
    }

    @Override
    public Object[] toArray()
    {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray( T[] ts )
    {
        return null;
    }

    @Override
    public boolean add( T t )
    {
        return false;
    }

    @Override
    public boolean remove( Object o )
    {
        return false;
    }

    @Override
    public boolean containsAll( Collection<?> objects )
    {
        return false;
    }

    @Override
    public boolean addAll( Collection<? extends T> ts )
    {
        return false;
    }

    @Override
    public boolean retainAll( Collection<?> objects )
    {
        return false;
    }

    @Override
    public boolean removeAll( Collection<?> objects )
    {
        return false;
    }

    @Override
    public void clear()
    {
    }
}
