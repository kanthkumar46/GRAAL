package edu.graal.utils;

import edu.immutablessupport.styles.SingletonStyle;
import edu.jgraphtsupport.AbstractEdge;
import edu.jgraphtsupport.AbstractVertex;
import edu.orca.algorithm.Orca;
import javaslang.Function1;
import org.immutables.value.Value;
import org.jgrapht.UndirectedGraph;

@Value.Immutable
@SingletonStyle
public abstract class SignatureProvider {
    private final Orca orca = new Orca();
    private static final int GRAPHLET_SIZE = 5;

    /**
     * utility method to get the signature of a node (or vertex) in the given graph
     *
     * @param graph host graph
     * @param vertex vertex in the graph
     * @param <T> graph vertex type
     * @param <U> graph edge type
     * @return 73-dimensional array representing signature of the vertex
     */
    public <T extends AbstractVertex, U extends AbstractEdge<T>> long[] getSignature(UndirectedGraph<T, U> graph,
                                                                                     T vertex) {
        final int vertexId = vertex.getVertexId();
        long[][] signatureVector = signatureVectorFunction.apply(graph);

        return signatureVector[vertexId];
    }

    /**
     * memoized version of {@code getSignatureVector} function.
     * computes signature vector (2-d array) of an undirected graph and memoizes it.
     */
    public final Function1<UndirectedGraph, long[][]> signatureVectorFunction =
            Function1.<UndirectedGraph, long[][]>of(this::getSignatureVector).memoized();

    /**
     * utility method to calculate the 73-dimensional signature of every node in the graph {node id's ranging
     * from 0 to n-1} using orbit counting algorithm.
     *
     * @param graph host graph whose signature vector needs to be computed
     * @param <T> graph vertex type
     * @param <U> graph edge type
     * @return 2-d vector, row index corresponding to node id (or vertex id) represents signature of that node
     */
    private <T extends AbstractVertex, U extends AbstractEdge<T>> long[][]
    getSignatureVector(final UndirectedGraph<T, U> graph) {
        orca.init(GRAPHLET_SIZE, graph);
        return orca.count();
    }
}