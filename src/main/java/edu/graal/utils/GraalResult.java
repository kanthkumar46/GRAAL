package edu.graal.utils;

import edu.graal.graphs.types.PDGEdge;
import edu.graal.graphs.types.PDGVertex;
import edu.immutablessupport.styles.BuilderStyle;
import javaslang.Function1;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Array;
import javaslang.collection.Map;
import org.immutables.value.Value;
import org.jgrapht.UndirectedGraph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by KanthKumar on 3/31/17.
 */
@Value.Immutable
@BuilderStyle
public abstract class GraalResult {
    public abstract Map<Tuple2, List<List<Tuple2<PDGVertex, PDGVertex>>>> getAlignments();
    public abstract Map<Tuple2<PDGVertex, PDGVertex>, Double> getOriginalAligningCosts();
    public abstract Map<Tuple2<PDGVertex, PDGVertex>, Double> getPdgAligningCosts();

    public Array<Tuple2<PDGVertex, PDGVertex>> findBestAlignment() {
        Function1<List<Tuple2<PDGVertex, PDGVertex>>, Double> alignmentCost = alignment -> alignment.stream()
                .map(tuple -> getPdgAligningCosts().get(tuple).get())
                .reduce(0.0, (a, b) -> a + b);

        List<Tuple2<PDGVertex, PDGVertex>> bestAlignment = getAlignments().values()
                .flatMap(Function.identity())
                .map(alignment -> Tuple.of(alignment, alignmentCost.apply(alignment)))
                .minBy(Comparator.comparingDouble(value -> value._2))
                .get()._1;

        return Array.ofAll(bestAlignment);
    }

    public int findEdgeCorrectness(final UndirectedGraph<PDGVertex, PDGEdge> original,
                                   final UndirectedGraph<PDGVertex, PDGEdge> suspect,
                                   final Array<Tuple2<PDGVertex, PDGVertex>> alignment) {
       Array<Tuple2<Set<PDGEdge>, Set<PDGEdge>>> edgeAlignment = alignment
                .map(tuple -> Tuple.of(original.edgesOf(tuple._1), suspect.edgesOf(tuple._2)));

        Set<Tuple2<PDGEdge, PDGEdge>> alignedEdges = new HashSet<>();
        Predicate<Tuple2<PDGEdge, PDGEdge>> notAligned = tuple -> alignedEdges.stream()
                .flatMap(edgeTuple -> Stream.of(edgeTuple._1, edgeTuple._2))
                .noneMatch(alignedEdge -> alignedEdge.equals(tuple._1) || alignedEdge.equals(tuple._2));

        for (Tuple2<Set<PDGEdge>, Set<PDGEdge>> tuple : edgeAlignment) {
            Array<Tuple2<PDGEdge, PDGEdge>> array = tuple._1.stream()
                    .flatMap(uEdge -> tuple._2.stream().map(vEdge -> Tuple.of(uEdge, vEdge)))
                    .collect(Array.collector());

            for (Tuple2<PDGEdge, PDGEdge> pdgEdgeTuple : array) {
                if(notAligned.test(pdgEdgeTuple) && pdgEdgeTuple._1.getType().equals(pdgEdgeTuple._2.getType()) &&
                        totalDegree(original, pdgEdgeTuple._1) == totalDegree(suspect, pdgEdgeTuple._2)) {
                    alignedEdges.add(pdgEdgeTuple);
                }
            }
        }

        float alignedEdgesRatio = (float) alignedEdges.size() /
                Math.min(original.edgeSet().size(), suspect.edgeSet().size());
        return Math.round(alignedEdgesRatio * 100);
    }

    private int totalDegree(UndirectedGraph<PDGVertex, PDGEdge> graph, PDGEdge edge) {
        return graph.degreeOf(edge.getSourceVertex()) + graph.degreeOf(edge.getTargetVertex());
    }
}
