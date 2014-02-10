package org.neo4j.neode.properties;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.*;

class IndexableProperty extends Property
{
    private Label label;
    private final String propertyName;
    private final PropertyValueGenerator generator;
    private final List<Label> labels;

    IndexableProperty( GraphDatabaseService db, String label, String propertyName, PropertyValueGenerator generator, String...labelNames )
    {
        this.label = DynamicLabel.label(label);
        this.propertyName = propertyName;
        this.generator = generator;
        this.labels = new ArrayList<>(labelNames.length);
        for (String labelName : labelNames) {
            this.labels.add(DynamicLabel.label(labelName));
        }
        createIndexes(db,this.label,propertyName,this.labels);
    }

    private void createIndexes(GraphDatabaseService db, Label label, String propertyName, List<Label> labelNames) {
        try ( Transaction tx = db.beginTx() )
        {
            db.schema().indexFor( label ).on( propertyName ).create();
            for ( Label l : labels )
            {
                db.schema().indexFor( l ).on( propertyName ).create();
            }
            tx.success();
        }
    }

    @Override
    public void setProperty( PropertyContainer propertyContainer, GraphDatabaseService db, String labelName,
                             int iteration )
    {
        Object value = generator.generateValue( propertyContainer, labelName, iteration );
        propertyContainer.setProperty( propertyName, value );

        indexProperty( propertyContainer, db, label, value );
        if (!labelName.equals(label.name())) {
            indexProperty( propertyContainer, db, DynamicLabel.label(labelName), value );
        }
        for ( Label label : labels)
        {
            indexProperty( propertyContainer, db, label, value );
        }
    }

    private void indexProperty( PropertyContainer propertyContainer, GraphDatabaseService db, Label label,
                                Object value )
    {
        if ( propertyContainer instanceof Node )
        {
            Node node = (Node) propertyContainer;
            if (! node.hasLabel(label)) {
                node.addLabel(label);
            }
        }
        else
        {
            throw new UnsupportedOperationException("No schema indexes for relationships");
        }
    }
}
