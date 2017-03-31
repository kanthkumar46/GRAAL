package edu.graal.utils;

import edu.graal.graphs.types.PDGVertex;
import edu.graal.graphs.types.VertexType;
import edu.immutablessupport.styles.SingletonStyle;
import edu.jgraphtsupport.AbstractEdge;
import edu.jgraphtsupport.AbstractVertex;
import javaslang.Function2;
import javaslang.Function3;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Array;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import org.immutables.value.Value;
import org.jgrapht.UndirectedGraph;

import java.util.Set;

import static edu.jgraphtsupport.GraphUtils.getMaxDegree;
import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.max;

/**
 * Created by KanthKumar on 3/13/17.
 */
@Value.Immutable
@SingletonStyle
public abstract class GraphAligner {

    /**
     * method to compute costs for aligning each vertex 'u' from original graph with every vertex 'v' from suspect graph
     * using the cost function defined in GRAAL(GRAph ALigner)
     *
     * @param original original graph to be aligned with suspect graph
     * @param suspect suspect graph to align with original graph
     * @param <T> graph vertex type
     * @param <U> graph edge type
     * @return sorted cost array: sorted costs for aligning each vertex 'u' from original graph with every vertex 'v' from suspect graph
     */
    public <T extends AbstractVertex, U extends AbstractEdge<T>>
    Map<Tuple2<T, T>, Double> computeAligningCosts(final double alpha,
                                                   final UndirectedGraph<T, U> original,
                                                   final UndirectedGraph<T, U> suspect) {
        Set<T> o = original.vertexSet();
        Set<T> s = suspect.vertexSet();
        int oGraphMaxDegree = getMaxDegree(original).getOrElse(1);
        int sGraphMaxDegree = getMaxDegree(suspect).getOrElse(1);

        SignatureProvider signatureProvider = ImmutableSignatureProvider.of();
        Function2<UndirectedGraph<T, U>, T, long[]> signatureFunction = signatureProvider::getSignature;

        Function2<T, T, Double> degreeFunction = (u, v) ->
                (original.degreeOf(u) + suspect.degreeOf(v)) / (double) (oGraphMaxDegree + sGraphMaxDegree);

        Function2<T, T, Double> signatureSimilarityFunction = (u, v) ->
                1 - distanceFunction.apply(signatureFunction.apply(original, u), signatureFunction.apply(suspect, v));

        Function2<T, T, Double> costFunction = (u, v) ->
                2 - ((1 - alpha) * degreeFunction.apply(u, v) + alpha * signatureSimilarityFunction.apply(u, v));

        return o.stream().flatMap(u -> s.stream().map(v -> Tuple.of(u, v)))
                .map(tuple -> Tuple.of(tuple, costFunction.tupled().apply(tuple)))
                .collect(HashMap.collector());
    }

    public Map<Tuple2<PDGVertex, PDGVertex>, Double>
    PDGAligningCosts(final double beta, final Map<Tuple2<PDGVertex, PDGVertex>, Double> originalAligningCosts) {
        Function2<Double, Double, Double> newCost = (originalCost, penalty) ->
                beta * originalCost + (1 - beta) * penalty;

        return originalAligningCosts
                .map((tuple, cost) -> Tuple.of(tuple, newCost.apply(cost, VertexType.getPenalty(tuple._1, tuple._2))));
    }

    /**
     * memoized version of {@code computeDistance} function.
     * computes distance between two nodes given their signatures and memoizes it.
     */
    public final Function2<long[], long[], Double>
            distanceFunction = Function2.of(this::computeDistance).memoized();

    private final Function3<Double, Long, Long, Double>
            graalOrbitDistanceFunction = (wi, ui, vi) -> wi * (abs(log(ui + 1) - log(vi + 1)) / log(max(ui, vi) + 2));
    /**
     * utility method to compute vertex distance using {@code graalDistanceFunction}
     *
     * @param uSignature signature of vertex 'u' in original graph
     * @param vSignature signature of vertex 'v' in suspect graph
     * @return computed distance between vertex 'u' and 'v' using Graal distance equation
     */
    private double computeDistance(final long[] uSignature, final long[] vSignature) {
        GraphletsUtil graphletsUtil = ImmutableGraphletsUtil.of();
        Array<Double> weight = graphletsUtil.getOrbitsWeight();

        double totalDistance = Array.range(0, 73)
                .map(i ->  graalOrbitDistanceFunction.apply(weight.get(i), uSignature[i], vSignature[i]))
                .sum().doubleValue();

        return (totalDistance / graphletsUtil.getTotalOrbitsWeight());
    }
}
