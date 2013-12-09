package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.SuperState;


public class Index extends Node {

    public Node value;


    public Index(Node n, int start, int end) {
        super(start, end);
        this.value = n;
        addChildren(n);
    }


    @NotNull
    @Override
    public SuperState transform(SuperState s) {
        return transformExpr(value, s);
    }


    @NotNull
    @Override
    public String toString() {
        return "<Index:" + value + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(value, v);
        }
    }
}
