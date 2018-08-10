package edu.graal.graphs.types;

import edu.immutablessupport.styles.BuilderStyle;
import edu.jgraphtsupport.AbstractVertex;
import javaslang.collection.Set;
import javaslang.control.Option;
import org.immutables.value.Value;

@Value.Immutable
@BuilderStyle
public abstract class PDGVertex implements AbstractVertex {
	public abstract IVertexType getType();
    public abstract Set<IVertexSubtype> subTypes();
	public abstract String getLabel();
	public abstract Set<String> getReadingVariables();
	public abstract Option<String> getAssignedVariable();

	/**
	 * By default Immutables will override equals and hashcode methods to use the attributes of this class
	 * (vertexId, type, label...). To avoid this; equals and hashcode methods are overridden to call Object's
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