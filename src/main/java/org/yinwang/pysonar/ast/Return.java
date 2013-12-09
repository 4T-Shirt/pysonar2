package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.SuperState;


public class Return extends Node {

    public Node value;


    public Return(Node n, int start, int end) {
        super(start, end);
        this.value = n;
        addChildren(n);
    }


    @NotNull
    @Override
    public SuperState transform(SuperState s) {
        if (value == null) {
            return Analyzer.self.builtins.None;
        } else {
            return transformExpr(value, s);
        }
    }


    @NotNull
    @Override
    public String toString() {
        return "<Return:" + value + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(value, v);
        }
    }
}
