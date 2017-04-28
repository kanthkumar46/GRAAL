package edu.graal;

import edu.graal.antlr.PDGListener;
import edu.graal.antlr4.Java8Lexer;
import edu.graal.antlr4.Java8Parser;
import edu.graal.graphs.ImmutablePDGraph;
import edu.graal.graphs.types.PDGVertex;
import edu.graal.graphs.PDGraph;
import edu.graal.antlr.ErrorListener;
import edu.graal.graphs.types.PDGEdge;
import edu.immutablessupport.styles.SingletonStyle;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.immutables.value.Value;

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
        return pdg;
    }

}
