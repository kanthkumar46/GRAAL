package com.graal.graphs;

import com.graal.graphs.types.GraphletEdge;
import com.graal.graphs.types.GraphletVertex;
import com.immutablessupport.styles.BuilderStyle;
import com.jgraphtsupport.AbstractGraph;
import org.immutables.value.Value;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;

@Value.Immutable
@BuilderStyle
public abstract class Graphlet extends AbstractGraph<GraphletVertex, GraphletEdge> {

	@Override
	@Value.Default
	public DirectedGraph<GraphletVertex, GraphletEdge> getDefaultGraph() {
		return new DefaultDirectedGraph<>(GraphletEdge.class);
	}

	public static ImmutableGraphlet of() {
		return ImmutableGraphlet.builder().build();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}