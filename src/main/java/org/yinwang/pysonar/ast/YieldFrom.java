package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.State;
import org.yinwang.pysonar.types.ListType;
import org.yinwang.pysonar.types.Type;


public class YieldFrom extends Node {

    public Node value;


    public YieldFrom(Node n, int start, int end) {
        super(start, end);
        this.value = n;
        addChildren(n);
    }


    @NotNull
    @Override
    public Type resolve(State s) {
        if (value != null) {
            return new ListType(resolveExpr(value, s));
        } else {
            return Analyzer.self.builtins.None;
        }
    }


    @NotNull
    @Override
    public String toString() {
        return "<YieldFrom:" + start + ":" + value + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(value, v);
        }
    }
}
