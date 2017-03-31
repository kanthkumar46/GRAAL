package edu.graal.utils;

import edu.jgraphtsupport.Edge;
import edu.jgraphtsupport.GraphUtils;
import edu.jgraphtsupport.ImmutableEdge;
import edu.jgraphtsupport.Vertex;
import edu.junitsupport.TestSetup;
import javaslang.Tuple2;
import javaslang.collection.Map;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by KanthKumar on 3/13/17.
 */
public class GraphAlignerTest extends TestSetup {
    private static final GraphAligner GRAPH_ALIGNER = ImmutableGraphAligner.of();

    @Test
    public void computeAligningCostsTest() {
        SimpleGraph<Vertex, Edge> simpleGraph1 = new SimpleGraph<>(Edge.class);
        SimpleGraph<Vertex, Edge> simpleGraph2 = new SimpleGraph<>(Edge.class);

        GraphUtils.addEdgeWithVertices(simpleGraph1, ImmutableEdge.of(0, 1));
        GraphUtils.addEdgeWithVertices(simpleGraph1, ImmutableEdge.of(1, 2));
        GraphUtils.addEdgeWithVertices(simpleGraph1, ImmutableEdge.of(2, 3));
        GraphUtils.addEdgeWithVertices(simpleGraph1, ImmutableEdge.of(3, 4));

        GraphUtils.addEdgeWithVertices(simpleGraph2, ImmutableEdge.of(0, 1));
        GraphUtils.addEdgeWithVertices(simpleGraph2, ImmutableEdge.of(1, 2));
        GraphUtils.addEdgeWithVertices(simpleGraph2, ImmutableEdge.of(2, 3));
        GraphUtils.addEdgeWithVertices(simpleGraph2, ImmutableEdge.of(3, 4));

        Map<Tuple2<Vertex, Vertex>, Double> map = GRAPH_ALIGNER
                .computeAligningCosts(0.8, simpleGraph1, simpleGraph2);
        Assert.assertEquals(map.size(), simpleGraph1.vertexSet().size() * simpleGraph2.vertexSet().size());
    }
}
