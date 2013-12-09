package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.State;
import org.yinwang.pysonar.SuperState;
import org.yinwang.pysonar.types.Type;
import org.yinwang.pysonar.types.UnionType;

import java.util.ArrayList;
import java.util.List;


public abstract class Node implements java.io.Serializable {

    public int start = -1;
    public int end = -1;

    @Nullable
    protected Node parent = null;


    public Node() {
    }


    public Node(int start, int end) {
        this.start = start;
        this.end = end;
    }


    public void setParent(Node parent) {
        this.parent = parent;
    }


    @Nullable
    public Node getParent() {
        return parent;
    }


    @NotNull
    public Node getAstRoot() {
        if (parent == null) {
            return this;
        }
        return parent.getAstRoot();
    }


    public int length() {
        return end - start;
    }


    public boolean bindsName() {
        return false;
    }


    @Nullable
    public String getFile() {
        return parent != null ? parent.getFile() : null;
    }


    public void addChildren(@Nullable Node... nodes) {
        if (nodes != null) {
            for (Node n : nodes) {
                if (n != null) {
                    n.setParent(this);
                }
            }
        }
    }


    public void addChildren(@Nullable List<? extends Node> nodes) {
        if (nodes != null) {
            for (Node n : nodes) {
                if (n != null) {
                    n.setParent(this);
                }
            }
        }
    }


    @Nullable
    public Str getDocString() {
        Node body = null;
        if (this instanceof FunctionDef) {
            body = ((FunctionDef) this).body;
        } else if (this instanceof ClassDef) {
            body = ((ClassDef) this).body;
        } else if (this instanceof Module) {
            body = ((Module) this).body;
        }

        if (body instanceof Block && ((Block) body).seq.size() >= 1) {
            Node firstExpr = ((Block) body).seq.get(0);
            if (firstExpr instanceof Expr) {
                Node docstrNode = ((Expr) firstExpr).value;
                if (docstrNode != null && docstrNode instanceof Str) {
                    return (Str) docstrNode;
                }
            }
        }
        return null;
    }


    @NotNull
    public static SuperState transformExpr(@NotNull Node n, SuperState s) {
        return n.transform(s);
    }


    @NotNull
    protected abstract SuperState transform(SuperState s);


    public boolean isCall() {
        return this instanceof Call;
    }


    public boolean isModule() {
        return this instanceof Module;
    }


    public boolean isClassDef() {
        return false;
    }


    public boolean isFunctionDef() {
        return false;
    }


    public boolean isLambda() {
        return false;
    }


    public boolean isName() {
        return this instanceof Name;
    }


    public boolean isGlobal() {
        return this instanceof Global;
    }


    public boolean isBinOp() {
        return this instanceof BinOp;
    }


    @NotNull
    public BinOp asBinOp() {
        return (BinOp) this;
    }


    @NotNull
    public Call asCall() {
        return (Call) this;
    }


    @NotNull
    public Module asModule() {
        return (Module) this;
    }


    @NotNull
    public ClassDef asClassDef() {
        return (ClassDef) this;
    }


    @NotNull
    public FunctionDef asFunctionDef() {
        return (FunctionDef) this;
    }


    @NotNull
    public Lambda asLambda() {
        return (Lambda) this;
    }


    @NotNull
    public Name asName() {
        return (Name) this;
    }


    @NotNull
    public Global asGlobal() {
        return (Global) this;
    }


    protected void addWarning(String msg) {
        Analyzer.self.putProblem(this, msg);
    }


    protected void addError(String msg) {
        Analyzer.self.putProblem(this, msg);
    }


    /**
     * Utility method to resolve every node in {@code nodes} and
     * return the union of their types.  If {@code nodes} is empty or
     * {@code null}, returns a new {@link org.yinwang.pysonar.types.UnknownType}.
     */
    @NotNull
    protected Type resolveListAsUnion(@Nullable List<? extends Node> nodes, SuperState s) {
        if (nodes == null || nodes.isEmpty()) {
            return Analyzer.self.builtins.unknown;
        }

        Type result = Analyzer.self.builtins.unknown;
        for (Node node : nodes) {
            Type nodeType = transformExpr(node, s);
            result = UnionType.union(result, nodeType);
        }
        return result;
    }


    /**
     * Resolves each element of a node list in the passed scope.
     * Node list may be empty or {@code null}.
     */
    static protected void resolveList(@Nullable List<? extends Node> nodes, State s) {
        if (nodes != null) {
            for (Node n : nodes) {
                transformExpr(n, s);
            }
        }
    }


    @Nullable
    static protected List<Type> resolveAndConstructList(@Nullable List<? extends Node> nodes, State s) {
        if (nodes == null) {
            return null;
        } else {
            List<Type> typeList = new ArrayList<>();
            for (Node n : nodes) {
                typeList.add(transformExpr(n, s));
            }
            return typeList;
        }
    }


    public String toDisplay() {
        return "";
    }


    public abstract void visit(NodeVisitor visitor);


    protected void visitNode(@Nullable Node n, NodeVisitor v) {
        if (n != null) {
            n.visit(v);
        }
    }


    protected void visitNodeList(@Nullable List<? extends Node> nodes, NodeVisitor v) {
        if (nodes != null) {
            for (Node n : nodes) {
                if (n != null) {
                    n.visit(v);
                }
            }
        }
    }
}
