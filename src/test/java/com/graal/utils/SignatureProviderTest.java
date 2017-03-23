package com.graal.utils;

import com.graal.ImmutablePDGGenerator;
import com.graal.PDGGenerator;
import com.graal.graphs.PDGraph;
import com.graal.graphs.types.PDGEdge;
import com.graal.graphs.types.PDGVertex;
import com.jgraphtsupport.*;
import com.junitsupport.TestSetup;
import javaslang.collection.Array;
import javaslang.collection.HashSet;
import javaslang.collection.Set;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static com.junitsupport.TestUtils.getResourceStream;
import static org.apache.commons.io.IOUtils.readLines;

/**
 * Created by KanthKumar on 3/6/17.
 */
public class SignatureProviderTest extends TestSetup {
    private static final SignatureProvider SIGNATURE_PROVIDER = ImmutableSignatureProvider.of();
    private SimpleGraph<Vertex, Edge> simpleGraph = new SimpleGraph<>(Edge.class);

    @Before
    public void clearGraph() {
        Set<Edge> edges = HashSet.ofAll(simpleGraph.edgeSet());
        simpleGraph.removeAllEdges(edges.toJavaSet());
    }

    @Test
    public void getGraphlet9SignatureTest() throws IOException {
        Vertex v0 = ImmutableVertex.of(0);

        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(0, 1));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(1, 2));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(2, 3));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(3, 4));

        SIGNATURE_PROVIDER.getSignature(simpleGraph, v0);
        long[][] vector = SIGNATURE_PROVIDER.signatureVectorFunction.apply(simpleGraph);

        Array<String> actualResult = Array.of(vector).map(longs -> Array.ofAll(longs).mkString(" "));

        List<String> expectedResult = readLines(getResourceStream("/signature_vector/graphlet9_5node.out"),
                Charset.defaultCharset());

        Assert.assertTrue(actualResult.eq(expectedResult));
    }

    @Test
    public void getGraphlet23SignatureTest() throws IOException {
        Vertex v0 = ImmutableVertex.of(0);

        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(0, 1));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(0, 2));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(0, 3));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(1, 2));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(1, 3));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(2, 3));
        GraphUtils.addEdgeWithVertices(simpleGraph, ImmutableEdge.of(0, 4));

        SIGNATURE_PROVIDER.getSignature(simpleGraph, v0);
        long[][] vector = SIGNATURE_PROVIDER.signatureVectorFunction.apply(simpleGraph);

        Array<String> actualResult = Array.of(vector).map(longs -> Array.ofAll(longs).mkString(" "));

        List<String> expectedResult = readLines(getResourceStream("/signature_vector/graphlet23_5node.out"),
                Charset.defaultCharset());

        Assert.assertTrue(actualResult.eq(expectedResult));
    }

    @Test
    public void getPDGSignatureTest() throws IOException {
        String testcode = "class Odd {\n" +
                "\tpublic int countOdd(int[] input) {\n" +
                "\t\tint oddCount = 0;\n" +
                "\t\tfor (int i = 0; i < input.length; i++) {\n" +
                "\t\t\tif (input[i] % 2 != 0) {\n" +
                "\t\t\t\toddCount++;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t\treturn oddCount;\n" +
                "\t}" +
                "}";
        PDGGenerator pdgGenerator = ImmutablePDGGenerator.of();
        PDGraph pdGraph = pdgGenerator.createPDG(testcode , 0);

        long[][] vector = SIGNATURE_PROVIDER.signatureVectorFunction.apply(pdGraph.getAsUndirectedGraphWithoutLoops());
        Assert.assertEquals(pdGraph.getSize(), vector.length);
    }

}
