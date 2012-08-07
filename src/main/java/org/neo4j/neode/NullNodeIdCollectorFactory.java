package org.neo4j.neode;

class NullNodeIdCollectorFactory implements NodeIdCollectorFactory
{
    @Override
    public NodeIdCollector createNodeIdCollector()
    {
        return NullNodeIdCollector.INSTANCE;
    }
}
