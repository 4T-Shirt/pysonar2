package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.SuperState;


public class Starred extends Node {

    public Node value;


    public Starred(Node n, int start, int end) {
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
        return "<starred:" + value + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(value, v);
        }
    }
}
