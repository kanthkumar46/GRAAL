package edu.graal.antlr;

import edu.graal.antlr4.Java8BaseListener;
import edu.graal.antlr4.Java8Parser;
import edu.graal.graphs.types.EdgeType;
import edu.graal.graphs.types.ImmutablePDGEdge;
import edu.graal.graphs.types.ImmutablePDGVertex;
import edu.graal.graphs.types.PDGEdge;
import edu.graal.graphs.types.PDGVertex;
import edu.graal.graphs.types.VertexSubtype;
import edu.graal.graphs.types.VertexType;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by KanthKumar on 3/19/17.
 */
public class PDGListener extends Java8BaseListener {
    // TODO 0: Need to take the scope of variables into account!

    private String currentMethod = "GLOBAL";

    private Map<String, List<PDGVertex>> vertices = new HashMap<>();
    private Map<String, List<PDGEdge>> edges = new HashMap<>();

    private Map<String, PDGVertex> lastAppearanceOfVariables = new HashMap<>();
    private int vertexCounter = 0;
    private Stack<PDGVertex> controlStack = new Stack<>();

    private List<String> errorMessages = new ArrayList<>();

    private int errorLineOffset;

    public PDGListener(int errorLineOffset) {
        this.errorLineOffset = errorLineOffset;
        vertices.put(currentMethod, new ArrayList<>());
        edges.put(currentMethod, new ArrayList<>());
    }

    // METHODS & PARAMETERS.

    private boolean collectExpressionVars;

    // methodDeclarator: Identifier '(' formalParameterList? ')' dims?.
    @Override
    public void enterMethodDeclarator(Java8Parser.MethodDeclaratorContext ctx) {
        currentMethod = ctx.getChild(0).getText();
        vertices.put(currentMethod, new ArrayList<>());
        edges.put(currentMethod, new ArrayList<>());
        lastAppearanceOfVariables.clear();
        expressionVariables.clear();
        controlStack.clear();
    }

    @Override
    public void exitFormalParameter(Java8Parser.FormalParameterContext ctx) {
        dealWithDeclaration();
    }

    // FOR
    private PDGVertex delayedNode;
    private boolean forUpdate;

    @Override
    public void exitBasicForStatement(Java8Parser.BasicForStatementContext ctx) {
        controlStack.pop();
    }

    @Override
    public void exitForStatement(Java8Parser.ForStatementContext ctx) {
        if (delayedNode != null) {
            expressionVariables.addAll(delayedNode.getReadingVariables().toJavaSet());
            createDataEdges(delayedNode);
            lastAppearanceOfVariables.put(delayedNode.getAssignedVariable().get(), delayedNode);

            expressionVariables.clear();
            delayedNode = null;
        }
    }

    @Override
    public void enterForUpdate(Java8Parser.ForUpdateContext ctx) {
        forUpdate = true;
    }

    @Override
    public void exitForUpdate(Java8Parser.ForUpdateContext ctx) {
        forUpdate = false;
    }

    @Override
    public void enterForExpression(Java8Parser.ForExpressionContext ctx) {
        dealWithEnterLoop(ctx);
    }

    @Override
    public void exitForExpression(Java8Parser.ForExpressionContext ctx) {
        dealWithExitLoop(ctx);
    }

    private void dealWithEnterLoop(ParserRuleContext ctx) {
        collectExpressionVars = true;
    }

    private PDGVertex dealWithExitLoop(ParserRuleContext ctx) {
        PDGVertex v = dealWithExitCondition(ctx);

        // Create self edge.
        createEdge(v, v, EdgeType.CTRL);

        return v;
    }

    // WHILE

    @Override
    public void exitWhileStatement(Java8Parser.WhileStatementContext ctx) {
        controlStack.pop();
    }

    @Override
    public void enterWhileCondition(Java8Parser.WhileConditionContext ctx) {
        dealWithEnterLoop(ctx);
    }

    @Override
    public void exitWhileCondition(Java8Parser.WhileConditionContext ctx) {
        dealWithExitLoop(ctx);
    }

    // EXPRESSIONS
    private String varAssigned;
    private Set<String> expressionVariables = new HashSet<>();

    // expressionName : Identifier | ambiguousName '.' Identifier ;

    @Override
    public void enterExpressionName(Java8Parser.ExpressionNameContext ctx) {
        dealWithAmbiguousName(ctx);
    }

    // ambiguousName : Identifier | ambiguousName '.' Identifier ;

    @Override
    public void enterAmbiguousName(Java8Parser.AmbiguousNameContext ctx) {
        dealWithAmbiguousName(ctx);
    }

    private void dealWithAmbiguousName(ParserRuleContext ctx) {
        String var = ctx.getChild(0).getText();

        // If this is a variable, add to the set.
        if (collectExpressionVars && lastAppearanceOfVariables.containsKey(var)) {
            if (isLeftHandSide)
                varAssigned = var;
            else
                expressionVariables.add(var);
        }
    }

    @Override
    public void enterTypeName(Java8Parser.TypeNameContext ctx) {
        dealWithAmbiguousName(ctx);
    }

    // assignment : leftHandSide assignmentOperator expression ;

    private boolean isReadingAndWriting;

    // assignmentOperator : '=' | '*=' | '/=' | '%=' | '+=' | '-=' | '<<='
    // | '>>=' | '>>>=' | '&=' | '^=' | '|=' ;
    @Override
    public void enterAssignmentOperator(Java8Parser.AssignmentOperatorContext ctx) {
        isReadingAndWriting = !ctx.getText().equals("=");
    }

    @Override
    public void enterAssignment(Java8Parser.AssignmentContext ctx) {
        dealWitEnterAssignment(ctx);
    }

    @Override
    public void exitAssignment(Java8Parser.AssignmentContext ctx) {
        dealWitExitAssignment(ctx);
        isReadingAndWriting = false;
    }

    // TODO 0: Deal with arrays!

    private boolean isLeftHandSide;

    @Override
    public void enterLeftHandSide(Java8Parser.LeftHandSideContext ctx) {
        isLeftHandSide = true;
    }

    @Override
    public void exitLeftHandSide(Java8Parser.LeftHandSideContext ctx) {
        isLeftHandSide = false;
    }

    @Override
    public void enterPreIncrementExpression(Java8Parser.PreIncrementExpressionContext ctx) {
        dealWitEnterAssignment(ctx);
    }

    @Override
    public void exitPreIncrementExpression(Java8Parser.PreIncrementExpressionContext ctx) {
        dealWithIncrementsAndDecrements();
        dealWitExitAssignment(ctx);
    }

    @Override
    public void enterPreDecrementExpression(Java8Parser.PreDecrementExpressionContext ctx) {
        dealWitEnterAssignment(ctx);
    }

    @Override
    public void exitPreDecrementExpression(Java8Parser.PreDecrementExpressionContext ctx) {
        dealWithIncrementsAndDecrements();
        dealWitExitAssignment(ctx);
    }

    @Override
    public void enterPostIncrementExpression(Java8Parser.PostIncrementExpressionContext ctx) {
        dealWitEnterAssignment(ctx);
    }

    @Override
    public void exitPostIncrementExpression(Java8Parser.PostIncrementExpressionContext ctx) {
        dealWithIncrementsAndDecrements();
        dealWitExitAssignment(ctx);
    }

    @Override
    public void enterPostDecrementExpression(Java8Parser.PostDecrementExpressionContext ctx) {
        dealWitEnterAssignment(ctx);
    }

    @Override
    public void exitPostDecrementExpression(Java8Parser.PostDecrementExpressionContext ctx) {
        dealWithIncrementsAndDecrements();
        dealWitExitAssignment(ctx);
    }

    private void dealWithIncrementsAndDecrements() {
        // TODO 0: This will not work for a[i] = ...
        Iterator<String> it = expressionVariables.iterator();
        if(it.hasNext()) {
            varAssigned = it.next();
        }
    }

    private void dealWitEnterAssignment(ParserRuleContext ctx) {
        collectExpressionVars = true;
    }

    private void dealWitExitAssignment(ParserRuleContext ctx) {
        if (isReadingAndWriting)
            expressionVariables.add(varAssigned);
        Set<VertexSubtype> subtypes = getSubtypes(ctx.getText());
        PDGVertex v = createVertex(VertexType.ASSIGN, subtypes, ctx.getText(), varAssigned, expressionVariables);

        if (!forUpdate) {
            createDataEdges(v);
            lastAppearanceOfVariables.put(varAssigned, v);
        } else
            // If we are in the update part of a for loop, we need to delay this
            // node until the end of the statement.
            delayedNode = v;

        collectExpressionVars = false;
        expressionVariables.clear();
    }

    private void createDataEdges(PDGVertex v) {
        for (String var : expressionVariables)
            createEdge(lastAppearanceOfVariables.get(var), v, EdgeType.DATA);
    }

    // IF

    @Override
    public void exitIfThenStatement(Java8Parser.IfThenStatementContext ctx) {
        controlStack.pop();
    }

    @Override
    public void enterIfCondition(Java8Parser.IfConditionContext ctx) {
        dealWithEnterCondition(ctx);
    }

    @Override
    public void exitIfCondition(Java8Parser.IfConditionContext ctx) {
        dealWithExitCondition(ctx);
    }

    private void dealWithEnterCondition(ParserRuleContext ctx) {
        collectExpressionVars = true;
    }

    private PDGVertex dealWithExitCondition(ParserRuleContext ctx) {
        Set<VertexSubtype> subtypes = getSubtypes(ctx.getText());
        PDGVertex v = createVertex(VertexType.CTRL, subtypes, ctx.getText(), null, expressionVariables);
        createDataEdges(v);

        controlStack.push(v);

        collectExpressionVars = false;
        expressionVariables.clear();

        return v;
    }

    private Set<VertexSubtype> getSubtypes(String text) {
        Set<VertexSubtype> result = new HashSet<>();
        if (text.contains("<")) {
            result.add(VertexSubtype.LT);
            result.add(VertexSubtype.COMP);
        }
        if (text.contains(">")) {
            result.add(VertexSubtype.GT);
            result.add(VertexSubtype.COMP);
        }
        if (text.contains("<=")) {
            result.add(VertexSubtype.LEQ);
            result.add(VertexSubtype.COMP);
        }
        if (text.contains(">=")) {
            result.add(VertexSubtype.GEQ);
            result.add(VertexSubtype.COMP);
        }
        if (text.contains("==")) {
            result.add(VertexSubtype.EQ);
            result.add(VertexSubtype.COMP);
        }
        if (text.contains("!=")) {
            result.add(VertexSubtype.INEQ);
            result.add(VertexSubtype.COMP);
        }
        if (text.contains("%")) {
            result.add(VertexSubtype.MOD);
            result.add(VertexSubtype.MUL);
        }
        if (text.contains("&&")) {
            result.add(VertexSubtype.AND);
            result.add(VertexSubtype.LOGICAL);
        }
        if (text.contains("||")) {
            result.add(VertexSubtype.OR);
            result.add(VertexSubtype.LOGICAL);
        }
        if(text.contains("+")) {
            result.add(VertexSubtype.ADD);
        }
        if (text.contains("++")) {
            result.add(VertexSubtype.INCR);
        }
        if (text.contains("-")) {
            result.add(VertexSubtype.SUB);
        }
        if (text.contains("--")) {
            result.add(VertexSubtype.DECR);
        }
        if (text.contains("+=")) {
            result.add(VertexSubtype.SH_PLUS);
            result.add(VertexSubtype.ADD);
        }
        if (text.contains("-=")) {
            result.add(VertexSubtype.SH_MINUS);
            result.add(VertexSubtype.SUB);
        }
        if (text.contains(".print")) {
            result.add(VertexSubtype.PRINT);
        }
        if (text.contains("pow(")) {
            result.add(VertexSubtype.MUL);
        }
        if (text.contains("(") && text.contains(")")) {
            result.add(VertexSubtype.CALL);
        }
        return result;
    }

    // VARIABLES

    private String varDecl;
    private boolean declaredAndNotInitVar;

    // variableDeclarator : variableDeclaratorId ('=' variableInitializer)? ;
    @Override
    public void enterVariableDeclarator(Java8Parser.VariableDeclaratorContext ctx) {
        declaredAndNotInitVar = ctx.getChildCount() == 1;
        collectExpressionVars = true;
    }

    @Override
    public void exitVariableDeclarator(Java8Parser.VariableDeclaratorContext ctx) {
        if (declaredAndNotInitVar)
            dealWithDeclaration();
        else {
            varAssigned = varDecl;
            dealWitExitAssignment(ctx);
        }
        declaredAndNotInitVar = false;
    }

    private void dealWithDeclaration() {
        PDGVertex v = createVertex(VertexType.DECL, new HashSet<>(), varDecl, varDecl, new HashSet<>());
        lastAppearanceOfVariables.put(varDecl, v);
    }

    @Override
    public void enterVariableDeclaratorId(Java8Parser.VariableDeclaratorIdContext ctx) {
        varDecl = ctx.getText();
    }

    @Override
    public void enterVariableInitializer(Java8Parser.VariableInitializerContext ctx) {
        collectExpressionVars = true;
    }

    @Override
    public void exitVariableInitializer(Java8Parser.VariableInitializerContext ctx) {
        collectExpressionVars = false;
    }

    // METHOD INVOCATION
    @Override
    public void enterMethodInvocation(Java8Parser.MethodInvocationContext ctx) {
        collectExpressionVars = true;
    }

    @Override
    public void exitMethodInvocation(Java8Parser.MethodInvocationContext ctx) {
        Set<VertexSubtype> subtypes = getSubtypes(ctx.getText());
        PDGVertex v = createVertex(VertexType.CALL, subtypes, ctx.getText(), null, expressionVariables);
        createDataEdges(v);

        if (v.getAssignedVariable().isDefined())
            lastAppearanceOfVariables.put(v.getAssignedVariable().get(), v);

        collectExpressionVars = false;
        expressionVariables.clear();
    }

    // RETURN
    @Override
    public void enterReturnStatement(Java8Parser.ReturnStatementContext ctx) {
        collectExpressionVars = true;
    }

    @Override
    public void exitReturnStatement(Java8Parser.ReturnStatementContext ctx) {
        Set<VertexSubtype> subtypes = getSubtypes(ctx.getText());
        PDGVertex v = createVertex(VertexType.RETURN, subtypes, ctx.getText(), null, expressionVariables);
        createDataEdges(v);

        collectExpressionVars = false;
        expressionVariables.clear();
    }

    // BREAK
    @Override
    public void enterBreakStatement(Java8Parser.BreakStatementContext ctx) {
        Set<VertexSubtype> subtypes = getSubtypes(ctx.getText());
        createVertex(VertexType.BREAK, subtypes, ctx.getText(), null, new HashSet<>());
    }

    @Override
    public void enterDoStatement(Java8Parser.DoStatementContext ctx) {
        addNotSupportedErrorMsg("Do-While", ctx);
    }

    @Override
    public void enterLambdaExpression(Java8Parser.LambdaExpressionContext ctx) {
        addNotSupportedErrorMsg("Lambda", ctx);
    }

    @Override
    public void enterIfThenElseStatement(Java8Parser.IfThenElseStatementContext ctx) {
        addNotSupportedErrorMsg("If-Else", ctx);
    }

    @Override
    public void enterSwitchStatement(Java8Parser.SwitchStatementContext ctx) {
        addNotSupportedErrorMsg("Switch", ctx);
    }

    @Override
    public void enterEnhancedForStatement(Java8Parser.EnhancedForStatementContext ctx) {
        addNotSupportedErrorMsg("For", ctx);
    }

    @Override
    public void enterClassInstanceCreationExpression(Java8Parser.ClassInstanceCreationExpressionContext ctx) {
        addNotSupportedErrorMsg("New", ctx);
    }

    @Override
    public void enterClassInstanceCreationExpression_lf_primary(
            Java8Parser.ClassInstanceCreationExpression_lf_primaryContext ctx) {
        addNotSupportedErrorMsg("New", ctx);
    }

    @Override
    public void enterClassInstanceCreationExpression_lfno_primary(
            Java8Parser.ClassInstanceCreationExpression_lfno_primaryContext ctx) {
        addNotSupportedErrorMsg("New", ctx);
    }


    // AUX

    private PDGVertex createVertex(VertexType type, Set<VertexSubtype> subtypes, String lbl, String assignedVar,
                                Set<String> refVars) {
        PDGVertex v = ImmutablePDGVertex.builder().vertexId(vertexCounter++)
        .setValueAssignedVariable(assignedVar)
        .type(type)
        .setIterableSubTypes(subtypes)
        .label(lbl)
        .setIterableReadingVariables(refVars).build();
        vertices.get(currentMethod).add(v);

        if (!controlStack.isEmpty())
            createEdge(controlStack.peek(), v, EdgeType.CTRL);

        return v;
    }

    private PDGEdge createEdge(PDGVertex from, PDGVertex to, EdgeType type) {
        PDGEdge e = ImmutablePDGEdge.of(from, to, type);
        edges.get(currentMethod).add(e);
        return e;
    }

    // NOT SUPPORTED
    private void addNotSupportedErrorMsg(String structure, ParserRuleContext ctx) {
        errorMessages.add(
                structure + " in line " + (ctx.getStart().getLine() - errorLineOffset) + " is not currently supported");
    }

    // SYNTAX ERROR
    public void addSyntaxErrorMsg(int line, int charPositionInLine, String msg) {
        errorMessages.add(
                "Syntax error in line " + (line - errorLineOffset) + " and column " + charPositionInLine + ": " + msg);
    }

    public Set<String> getMethods() {
        return vertices.keySet();
    }

    public List<PDGVertex> getVertices(String method) {
        return vertices.get(method);
    }

    public List<PDGEdge> getEdges(String method) {
        return edges.get(method);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
