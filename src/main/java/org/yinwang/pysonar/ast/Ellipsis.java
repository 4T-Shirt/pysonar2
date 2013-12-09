package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.SuperState;


public class Ellipsis extends Node {

    public Ellipsis(int start, int end) {
        super(start, end);
    }


    @NotNull
    @Override
    public String toString() {
        return "<Ellipsis>";
    }


    @NotNull
    @Override
    public SuperState transform(SuperState s) {
        return Analyzer.self.builtins.None;
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        v.visit(this);
    }
}
