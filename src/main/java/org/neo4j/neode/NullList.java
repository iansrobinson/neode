package org.neo4j.neode;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

final class NullList<T> implements List<T>
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
    public boolean addAll( int i, Collection<? extends T> ts )
    {
        return false;
    }

    @Override
    public boolean removeAll( Collection<?> objects )
    {
        return false;
    }

    @Override
    public boolean retainAll( Collection<?> objects )
    {
        return false;
    }

    @Override
    public void clear()
    {
    }

    @Override
    public T get( int i )
    {
        return null;
    }

    @Override
    public T set( int i, T t )
    {
        return null;
    }

    @Override
    public void add( int i, T t )
    {
    }

    @Override
    public T remove( int i )
    {
        return null;
    }

    @Override
    public int indexOf( Object o )
    {
        return 0;
    }

    @Override
    public int lastIndexOf( Object o )
    {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator()
    {
        return null;
    }

    @Override
    public ListIterator<T> listIterator( int i )
    {
        return null;
    }

    @Override
    public List<T> subList( int i, int i1 )
    {
        return null;
    }
}
