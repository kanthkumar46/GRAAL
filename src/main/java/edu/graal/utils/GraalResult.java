package edu.graal.utils;

import edu.graal.graphs.types.PDGVertex;
import edu.immutablessupport.styles.BuilderStyle;
import javaslang.Function1;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Map;
import org.immutables.value.Value;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by KanthKumar on 3/31/17.
 */
@Value.Immutable
@BuilderStyle
public abstract class GraalResult {
    public abstract Map<Tuple2, List<List<Tuple2<PDGVertex, PDGVertex>>>> getAlignments();
    public abstract Map<Tuple2<PDGVertex, PDGVertex>, Double> getOriginalAligningCosts();
    public abstract Map<Tuple2<PDGVertex, PDGVertex>, Double> getPdgAligningCosts();

    public List<Tuple2<PDGVertex, PDGVertex>> findBestAlignment() {
        Function1<List<Tuple2<PDGVertex, PDGVertex>>, Double> alignmentCost = alignment -> alignment.stream()
                .map(tuple -> getPdgAligningCosts().get(tuple).get())
                .reduce(0.0, (a, b) -> a + b);

        return getAlignments().values().flatMap(Function.identity())
                .map(alignment -> Tuple.of(alignment, alignmentCost.apply(alignment)))
                .minBy(Comparator.comparingDouble(value -> value._2))
                .get()._1;
    }
}
