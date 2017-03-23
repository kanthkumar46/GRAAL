package com.graal.utils;

import com.graal.graphs.types.PDGVertex;
import com.graal.graphs.types.VertexType;
import com.immutablessupport.styles.SingletonStyle;
import com.jgraphtsupport.AbstractEdge;
import com.jgraphtsupport.AbstractVertex;
import javaslang.*;
import javaslang.collection.Array;
import org.immutables.value.Value;
import org.jgrapht.UndirectedGraph;

import java.util.Comparator;
import java.util.Set;

import static com.jgraphtsupport.GraphUtils.getMaxDegree;
import static java.lang.Math.*;

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
    Array<Tuple3<T, T, Double>> computeAligningCosts(final double alpha,
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
                .map(tuple -> Tuple.of(tuple._1, tuple._2, costFunction.tupled().apply(tuple)))
                .collect(Array.collector())
                .sorted(Comparator.comparingDouble(tuple -> tuple._3));
    }

    public Array<Tuple3<PDGVertex, PDGVertex, Double>>
    PDGAligningCosts(final double beta, final Array<Tuple3<PDGVertex, PDGVertex, Double>> originalAligningCosts) {
        Function2<Double, Double, Double> newCost = (originalCost, typeSimilarity) ->
                beta * originalCost + (1 - beta) * typeSimilarity;

        return originalAligningCosts
                .map(tuple ->  tuple.map3(cost -> newCost.apply(cost, VertexType.getPenalty(tuple._1, tuple._2))))
                .sorted(Comparator.comparingDouble(tuple -> tuple._3));
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
