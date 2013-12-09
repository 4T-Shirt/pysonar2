package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.State;
import org.yinwang.pysonar.types.Type;


public class Continue extends Node {

    public Continue(int start, int end) {
        super(start, end);
    }


    @NotNull
    @Override
    public String toString() {
        return "<Continue>";
    }


    @NotNull
    @Override
    public Type resolve(State s) {
        return Analyzer.self.builtins.None;
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        v.visit(this);
    }
}
