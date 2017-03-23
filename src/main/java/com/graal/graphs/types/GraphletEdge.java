package com.graal.graphs.types;

import com.immutablessupport.styles.TupleStyle;
import com.jgraphtsupport.AbstractEdge;
import org.immutables.value.Value;

@Value.Immutable
@TupleStyle
public abstract class GraphletEdge extends AbstractEdge<GraphletVertex> {

    @Override
    public abstract GraphletVertex getSourceVertex();

    @Override
    public abstract GraphletVertex getTargetVertex();

    /**
     * By default Immutables will override equals and hashcode methods to use the attributes of this class
     * (sourceVertex, targetVertex). To avoid this; equals and hashcode methods are overridden to call Object's
     * (two edges are not equal unless it is same instance). P.S "I know this sucks"
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
