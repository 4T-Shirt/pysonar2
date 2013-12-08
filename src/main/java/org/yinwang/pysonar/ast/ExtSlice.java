package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.State;
import org.yinwang.pysonar.types.ListType;
import org.yinwang.pysonar.types.Type;

import java.util.List;


public class ExtSlice extends Node {

    public List<Node> dims;


    public ExtSlice(List<Node> dims, int start, int end) {
        super(start, end);
        this.dims = dims;
        addChildren(dims);
    }


    @NotNull
    @Override
    public Type resolve(State s) {
        for (Node d : dims) {
            resolveExpr(d, s);
        }
        return new ListType();
    }


    @NotNull
    @Override
    public String toString() {
        return "<ExtSlice:" + dims + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            for (Node d : dims) {
                visitNode(d, v);
            }
        }
    }
}
