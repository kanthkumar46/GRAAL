package com.graal.graphs.types;

import com.immutablessupport.styles.TupleStyle;
import com.jgraphtsupport.AbstractVertex;
import org.immutables.value.Value;

@Value.Immutable
@TupleStyle
public abstract class GraphletVertex implements AbstractVertex {

	@Override
	public abstract int getVertexId(); //re-declaring to maintain generated constructor arguments order

	public abstract int getOrbitNumber();

	/**
	 * By default Immutables will override equals and hashcode methods to use the attributes of this class
	 * (vertexId, orbitNumber). To avoid this; equals and hashcode methods are overridden to call Object's
	 * (two vertices are not equal unless it is same instance). P.S "I know this sucks"
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