package edu.graal;

import edu.graal.antlr.PDGListener;
import edu.graal.antlr4.Java8Lexer;
import edu.graal.antlr4.Java8Parser;
import edu.graal.graphs.ImmutablePDGraph;
import edu.graal.graphs.types.EdgeType;
import edu.graal.graphs.types.ImmutablePDGEdge;
import edu.graal.graphs.types.ImmutablePDGVertex;
import edu.graal.graphs.types.PDGVertex;
import edu.graal.graphs.PDGraph;
import edu.graal.antlr.ErrorListener;
import edu.graal.graphs.types.PDGEdge;
import edu.graal.graphs.types.VertexType;
import edu.immutablessupport.styles.SingletonStyle;
import edu.jgraphtsupport.GraphUtils;
import javaslang.collection.HashSet;
import javaslang.collection.Set;
import javaslang.control.Option;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.immutables.value.Value;
import org.jgrapht.DirectedGraph;

import java.util.Comparator;

/**
 *  Created by KanthKumar on 2/24/17.
 */
@Value.Immutable
@SingletonStyle
public abstract class PDGGenerator {

    public PDGraph createPDG(String body, int errorLineOffset) {
        Lexer lexer = new Java8Lexer(new ANTLRInputStream(body));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);

        PDGListener listener = new PDGListener(errorLineOffset);

        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener(listener));

        ParserRuleContext t = parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, t);

        PDGraph pdg = ImmutablePDGraph.of();
        for (String method : listener.getMethods()) {
            for (PDGVertex current : listener.getVertices(method)) {
                pdg.addVertex(current, method);
            }
            for (PDGEdge current : listener.getEdges(method)) {
                pdg.addEdge(current, method);
            }
        }

        constructSingleGraph(pdg);
        return pdg;
    }

    /**
     * TODO: {@code createPDG} method generates separate PDG for each method in the input program and this
     * method connects all the PDGs to construct a single PDG by choosing a vertex from each PDG that has zero
     * in-degree and max out-degree.
     * Either this method or PDGListener class needs to be updated to connect the vertices the represents function call
     * in the caller and first statement in callee function.
     *
     * @param pdg program dependence graphs constructed by {@code createPDG} method.
     */
    private static final String CONNECTION_VERTEX_LABEL = "Connection Vertex";
    private void constructSingleGraph(final PDGraph pdg) {
        int vertexId = pdg.getMethodGraphs().values().stream().map(GraphUtils::getMaxVertexId)
                .mapToInt(Option::get)
                .max().orElse(-1) + 1;

        if( pdg.getMethodGraphs().size() > 1){
            PDGVertex connectionVertex = ImmutablePDGVertex.builder()
                    .vertexId(vertexId)
                    .type(VertexType.CONN)
                    .label(CONNECTION_VERTEX_LABEL).build();

            pdg.addVertex(connectionVertex);
            pdg.getMethodGraphs().values().forEach(graph -> {
                PDGEdge connectionEdge = ImmutablePDGEdge.of(connectionVertex, getStartVertex(graph), EdgeType.CONN);
                pdg.addEdge(connectionEdge);
            });
        }
    }

    private PDGVertex getStartVertex(DirectedGraph<PDGVertex, PDGEdge> graph) {
        Set<PDGVertex> pdgVertexSet = HashSet.ofAll(graph.vertexSet());
        return pdgVertexSet
                .filter(pdgVertex -> graph.inDegreeOf(pdgVertex) == 0 && graph.outDegreeOf(pdgVertex) > 0)
                .maxBy(Comparator.comparingInt(graph::outDegreeOf))
                .getOrElse(pdgVertexSet.head());

    }
}
