package com.graal.graphs;

import com.graal.graphs.types.PDGVertex;
import com.graal.graphs.types.PDGEdge;
import com.immutablessupport.styles.BuilderStyle;
import com.jgraphtsupport.AbstractGraph;
import com.jgraphtsupport.GraphUtils;
import org.immutables.value.Value;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import java.util.HashMap;
import java.util.Map;

@Value.Immutable
@BuilderStyle
public abstract class PDGraph extends AbstractGraph<PDGVertex, PDGEdge> {

	private final Map<String, DirectedGraph<PDGVertex, PDGEdge>> methodGraphs = new HashMap<>();

	@Override
	@Value.Default
	public DirectedGraph<PDGVertex, PDGEdge> getDefaultGraph() {
		return new DefaultDirectedWeightedGraph<>(PDGEdge.class);
	}

	public void addEdge(PDGEdge newEdge, String methodName) {
		super.addEdge(newEdge);
		DirectedGraph<PDGVertex, PDGEdge> currentMethodGraph = methodGraphs
				.computeIfAbsent(methodName, key -> new DefaultDirectedWeightedGraph<>(PDGEdge.class));
		GraphUtils.addEdgeWithVertices(currentMethodGraph, newEdge);
	}

	public void addVertex(PDGVertex newVertex, String methodName) {
		super.addVertex(newVertex);
		methodGraphs.computeIfAbsent(methodName, key -> new DefaultDirectedWeightedGraph<>(PDGEdge.class))
					.addVertex(newVertex);
	}

	public static ImmutablePDGraph of() {
		return ImmutablePDGraph.builder().build();
	}

	@Override
	public String toString() {
		return super.toString();
	}
}