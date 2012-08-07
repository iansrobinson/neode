package org.neo4j.neode;

class UniqueNodeIdCollectorFactory implements NodeIdCollectorFactory
{
    @Override
    public NodeIdCollector createNodeIdCollector()
    {
        return new UniqueNodeIdCollector();
    }
}
