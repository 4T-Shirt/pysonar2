package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.State;
import org.yinwang.pysonar.types.ListType;
import org.yinwang.pysonar.types.Type;


public class Slice extends Node {

    public Node lower;
    public Node step;
    public Node upper;


    public Slice(Node lower, Node step, Node upper, int start, int end) {
        super(start, end);
        this.lower = lower;
        this.step = step;
        this.upper = upper;
        addChildren(lower, step, upper);
    }


    @NotNull
    @Override
    public Type resolve(State s) {
        if (lower != null) {
            resolveExpr(lower, s);
        }
        if (step != null) {
            resolveExpr(step, s);
        }
        if (upper != null) {
            resolveExpr(upper, s);
        }
        return new ListType();
    }


    @NotNull
    @Override
    public String toString() {
        return "<Slice:" + lower + ":" + step + ":" + upper + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(lower, v);
            visitNode(step, v);
            visitNode(upper, v);
        }
    }
}
