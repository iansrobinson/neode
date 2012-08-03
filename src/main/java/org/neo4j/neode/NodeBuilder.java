//package org.neo4j.neode;
//
//import static java.util.Arrays.asList;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//
//import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.Node;
//import org.neo4j.neode.properties.Property;
//
//public class NodeBuilder
//{
//    private String label;
//    private List<Property> properties = Collections.emptyList();
//
//    NodeBuilder( String label )
//    {
//        this.label = label;
//    }
//
//    public NodeBuilder withProperties( Property... values )
//    {
//        properties = asList( values );
//        return this;
//    }
//
//    public NodeSpecification build()
//    {
//        return new LabelledNodeSpecification( label, properties );
//    }
//
//    private class LabelledNodeSpecification extends NodeSpecification
//    {
//        private final String label;
//        private final List<Property> properties;
//
//        private LabelledNodeSpecification( String label, List<Property> properties )
//        {
//            this.label = label;
//            this.properties = properties;
//        }
//
//        @Override
//        public Node build( GraphDatabaseService db, int index, Random random )
//        {
//            Node node = db.createNode();
//            node.setProperty( "_label", label );
//            for ( Property property : properties )
//            {
//                property.setProperty( node, db, label, index, random );
//            }
//            return node;
//        }
//
//        @Override
//        public String label()
//        {
//            return label;
//        }
//    }
//
//}
