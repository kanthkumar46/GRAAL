package edu.graal;

import edu.graal.graphs.PDGraph;
import edu.graal.graphs.types.PDGEdge;
import edu.graal.graphs.types.PDGVertex;
import edu.graal.utils.GraphAligner;
import edu.graal.utils.ImmutableGraphAligner;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Array;
import javaslang.collection.HashMap;
import javaslang.collection.Map;
import javaslang.collection.Seq;
import javaslang.collection.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static edu.graal.graphs.types.VertexType.MAX_PENALTY;
import static java.util.Comparator.comparingDouble;

/**
 * Created by KanthKumar on 3/16/17.
 */
public class GraalAlgorithm {
    private GraphAligner graphAligner = ImmutableGraphAligner.of();

    private static final double SIGNATURE_SIMILARITY_CONTRIBUTION = 0.8;
    private static final double ORIGINAL_COST_CONTRIBUTION = 0.6;
    private static final BiPredicate<Tuple2, Double> LABEL_MATCH = (vertices, cost) ->
            cost < (1 - ORIGINAL_COST_CONTRIBUTION) * MAX_PENALTY;

    /**
     * Method that executes slightly modified version of GRAAL algorithm (considers all possible alignments)
     *
     * @param original program dependency graph of original program
     * @param suspect program dependency graph of suspect program
     */
    public Map<Tuple2, List<List<Tuple2<PDGVertex, PDGVertex>>>> execute(PDGraph original, PDGraph suspect) {
        Map<Tuple2<PDGVertex, PDGVertex>, Double> originalAligningCosts = graphAligner
                .computeAligningCosts(SIGNATURE_SIMILARITY_CONTRIBUTION, original.getAsUndirectedGraphWithoutLoops(),
                        suspect.getAsUndirectedGraphWithoutLoops());

        Map<Tuple2<PDGVertex, PDGVertex>, Double> pdgAligningCosts = graphAligner
                .PDGAligningCosts(ORIGINAL_COST_CONTRIBUTION, originalAligningCosts)
                .filter(LABEL_MATCH);

        java.util.Map<Tuple2, List<List<Tuple2<PDGVertex, PDGVertex>>>> alignmentsPerSeed = new java.util.HashMap<>();
        Set<Tuple2<PDGVertex, PDGVertex>> seeds = findSeeds(pdgAligningCosts);

        for(Tuple2<PDGVertex, PDGVertex> seed : seeds) {
            int sphereSize = 1;
            int radius = 1;
            List<List<Tuple2<PDGVertex, PDGVertex>>> alignments = Array.of(Array.of(seed).toJavaList()).toJavaList();
            Seq<Array<Tuple2<PDGVertex, PDGVertex>>> sphereMap;

            while(sphereSize != 0) {
                Array<PDGVertex> uSphere = makeSpheres(seed._1, original, radius);
                Array<PDGVertex> vSphere = makeSpheres(seed._2, suspect, radius);
                sphereSize = Math.min(uSphere.size(), vSphere.size());

                if(sphereSize != 0) {
                    sphereMap = mapSpheresAndSortByCost(uSphere, vSphere, pdgAligningCosts);
                    List<List<Tuple2<PDGVertex, PDGVertex>>> temp = new ArrayList<>();

                    for(List<Tuple2<PDGVertex, PDGVertex>> alignment : alignments) {
                        temp.addAll(alignSpheres(sphereMap, alignment));
                    }

                    alignments.clear();
                    alignments.addAll(temp);
                }

                radius++;
            }
            alignmentsPerSeed.put(seed, alignments);
        }

        return HashMap.ofAll(alignmentsPerSeed);
    }

    /**
     * utility method to find the seeds (pair of nodes) which has minimum aligning cost given the cost matrix that
     * describes the cost of aligning every vertex from one PDG to every vertex in other PDG.
     *
     * @param aligningCosts cost matrix
     * @return list of seeds that has minimum aligning cost
     */
    private Set<Tuple2<PDGVertex, PDGVertex>> findSeeds(Map<Tuple2<PDGVertex, PDGVertex>, Double>  aligningCosts) {
        final double min = aligningCosts.minBy(comparingDouble(tuple -> tuple._2)).get()._2;
        return aligningCosts.filterValues(cost -> cost.equals(min))
                     .keySet();
    }

    /**
     * utility method to create a sphere (set of nodes which are exactly at the distance 'r' from vertex/node 'u')
     * as described in GRAAL. This method leverages Dijkstra's Shortest Path algorithm to compute the sphere.
     *
     * @param u vertex 'u'
     * @param graph input graph to find set of nodes
     * @param radius distance 'r'
     *
     * @return Sphere; set of nodes that are exactly at the distance r from u.
     */
    private Array<PDGVertex> makeSpheres(PDGVertex u, PDGraph graph, double radius) {
        DijkstraShortestPath<PDGVertex, PDGEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(graph.getAsUndirectedGraphWithoutLoops(), radius);
        final SingleSourcePaths<PDGVertex, PDGEdge> singleSourcePaths = dijkstraShortestPath.getPaths(u);

        return graph.getDefaultGraph().vertexSet().stream()
                .filter(v -> !u.equals(v))
                .map(singleSourcePaths::getPath)
                .filter(Objects::nonNull)
                .filter(path -> path.getLength() == radius)
                .map(GraphPath::getEndVertex)
                .collect(Array.collector());
    }

    /**
     * maps every vertex 'u' of sphere1 with every vertex 'v' in sphere2 to create vertex pairs, prunes the pairs
     * the not present in pdg aligning cost map and sorts the pairs by cost of aligning them
     *
     * @param sphere1 sphere created out of PDG1
     * @param sphere2 sphere created out of PDG2
     * @param costMap PDG aligning cost map
     * @return array of node/vertex pair created out of spheres and grouped by vertex 'u'
     */
    private Seq<Array<Tuple2<PDGVertex, PDGVertex>>> mapSpheresAndSortByCost(Array<PDGVertex> sphere1,
                                                                             Array<PDGVertex> sphere2,
                                                                             Map<Tuple2<PDGVertex, PDGVertex>, Double> costMap) {
        final Function<Tuple2<PDGVertex, PDGVertex>, Double> cost = tuple -> costMap.get(tuple).get();
        Map<Tuple2<PDGVertex, Double>, Array<Tuple2<PDGVertex, PDGVertex>>> map  = sphere1
                .crossProduct(sphere2).toArray()
                .filter(costMap::containsKey)
                .sortBy(cost)
                .groupBy(tuple -> Tuple.of(tuple._1, cost.apply(tuple)));

        return map.values();
    }

    /**
     * given the map of spheres and an alignment, generates all possible alignments (occurs only if there are
     * node/vertex pairs in the sphere map that has same aligning cost) by aligning the pair of nodes
     * from the sphere map that are not already aligned in the current alignment.
     *
     * @param sphereMap array of node pair(u, v) {u from sphere1 and v from sphere2}
     * @param currentAlignment list of node pairs that are already aligned
     * @return list of alignments.
     */
    private List<List<Tuple2<PDGVertex, PDGVertex>>> alignSpheres(Seq<Array<Tuple2<PDGVertex, PDGVertex>>> sphereMap,
                                                                  List<Tuple2<PDGVertex, PDGVertex>> currentAlignment) {
        int depth = 0;
        List<List<Tuple2<PDGVertex, PDGVertex>>> alignments = new ArrayList<>();
        findAlignments(sphereMap, depth, alignments, currentAlignment);

        return alignments;
    }

    private void findAlignments(Seq<Array<Tuple2<PDGVertex, PDGVertex>>> sphereMap, int depth,
                                List<List<Tuple2<PDGVertex, PDGVertex>>> alignments,
                                List<Tuple2<PDGVertex, PDGVertex>> current) {
        if(sphereMap.size() == depth) {
            if(!alignments.contains(current)) alignments.add(current);
            return;
        }

        Predicate<Tuple2<PDGVertex, PDGVertex>> isAlreadyAligned = tuple -> current.stream()
                .flatMap(t -> Stream.of(t._1, t._2))
                .anyMatch(v -> v.equals(tuple._1) || v.equals(tuple._2));

        Array<Tuple2<PDGVertex, PDGVertex>> tuples = sphereMap.get(depth)
                .filter(isAlreadyAligned.negate());

        if(tuples.isEmpty()) {
            findAlignments(sphereMap, depth + 1, alignments, current);
        } else {
            tuples.forEach(tuple -> {
                List<Tuple2<PDGVertex, PDGVertex>> temp = Array.ofAll(current).append(tuple).toJavaList();
                findAlignments(sphereMap, depth + 1, alignments, temp);
            });
        }
    }

}
