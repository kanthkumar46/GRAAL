package edu.graal.graphs.types;

import edu.immutablessupport.styles.TupleStyle;
import edu.jgraphtsupport.AbstractEdge;
import org.immutables.value.Value;

@Value.Immutable
@TupleStyle
public abstract class PDGEdge extends AbstractEdge<PDGVertex> {

	@Override
	public abstract PDGVertex getSourceVertex(); //re-declaring to maintain generated constructor arguments order

	@Override
	public abstract PDGVertex getTargetVertex(); //re-declaring to maintain generated constructor arguments order

	public abstract EdgeType getType();

	/**
	 * By default Immutables will override equals and hashcode methods to use the attributes of this class
	 * (sourceVertex, targetVertex, type). To avoid this; equals and hashcode methods are overridden to call
	 * Object's (two edges are not equal unless it is same instance). P.S "I know this sucks"
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
