package com.graal;

import com.graal.graphs.PDGraph;
import com.graal.graphs.types.PDGEdge;
import com.graal.graphs.types.PDGVertex;
import com.graal.utils.GraphAligner;
import com.graal.utils.ImmutableGraphAligner;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.collection.Array;
import javaslang.control.Option;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

import static com.graal.graphs.types.VertexType.MAX_PENALTY;

/**
 * Created by KanthKumar on 3/16/17.
 */
public class GraalAlgorithm {
    private GraphAligner graphAligner = ImmutableGraphAligner.of();
    private static final double SIGNATURE_SIMILARITY_CONTRIBUTION = 0.8;
    private static final double ORIGINAL_COST_CONTRIBUTION = 0.6;

    private List<Tuple2<PDGVertex, PDGVertex>> alignedVertices = new ArrayList<>();
    private Predicate<Tuple2<PDGVertex, PDGVertex>> isAlreadyAligned = tuple -> alignedVertices.contains(tuple);

    public Array<Tuple2<PDGVertex, PDGVertex>> execute(PDGraph original, PDGraph suspect) {
        Array<Tuple3<PDGVertex, PDGVertex, Double>> originalAligningCosts = graphAligner
                .computeAligningCosts(SIGNATURE_SIMILARITY_CONTRIBUTION, original.getAsUndirectedGraphWithoutLoops(),
                        suspect.getAsUndirectedGraphWithoutLoops());

        //Compute new cost that considers PDG vertex labels and filter the alignments that has label mismatch
        Array<Tuple3<PDGVertex, PDGVertex, Double>> pdgAligningCosts = graphAligner
                .PDGAligningCosts(ORIGINAL_COST_CONTRIBUTION, originalAligningCosts)
                .filter(tuple -> tuple._3 < (1 - ORIGINAL_COST_CONTRIBUTION) * MAX_PENALTY);

        Queue<Tuple3<PDGVertex, PDGVertex, Double>> nodesToAlign = new PriorityQueue<>(Comparator.comparingDouble(t ->t._3));
        nodesToAlign.addAll(pdgAligningCosts.toJavaList());
        Tuple2<PDGVertex, PDGVertex> seed = findSeed(nodesToAlign);
        alignedVertices.add(seed);

        DoubleStream.of(1, 2, 3).forEach(radius -> {
            Array<PDGVertex> uSphere = makeSpheres(seed._1, original, radius);
            Array<PDGVertex> vSphere = makeSpheres(seed._2, suspect, radius);
            alignSpheres(uSphere, vSphere);
        });

        return Array.ofAll(alignedVertices);
    }

    private Tuple2<PDGVertex, PDGVertex> findSeed(Queue<Tuple3<PDGVertex, PDGVertex, Double>> nodesToAlign) {
        return nodesToAlign.poll().apply((u, v, cost) -> Tuple.of(u, v));
    }

    private Array<PDGVertex> makeSpheres(PDGVertex vertex, PDGraph graph, double radius) {
        DijkstraShortestPath<PDGVertex, PDGEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(graph.getAsUndirectedGraph(), radius);
        final SingleSourcePaths<PDGVertex, PDGEdge> singleSourcePaths = dijkstraShortestPath.getPaths(vertex);

        return graph.getDefaultGraph().vertexSet().stream()
                .filter(v -> !vertex.equals(v))
                .map(sink -> Option.of(singleSourcePaths.getPath(sink)))
                .filter(Option::isDefined)
                .map(path -> path.get().getEndVertex())
                .collect(Array.collector());
    }

    private void alignSpheres(Array<PDGVertex> sphere1, Array<PDGVertex> sphere2) {

    }
}
