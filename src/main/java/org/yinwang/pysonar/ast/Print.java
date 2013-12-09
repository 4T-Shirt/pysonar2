package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.SuperState;

import java.util.List;


public class Print extends Node {

    public Node dest;
    public List<Node> values;


    public Print(Node dest, List<Node> elts, int start, int end) {
        super(start, end);
        this.dest = dest;
        this.values = elts;
        addChildren(dest);
        addChildren(elts);
    }


    @NotNull
    @Override
    public SuperState transform(SuperState s) {
        if (dest != null) {
            transformExpr(dest, s);
        }
        if (values != null) {
            resolveList(values, s);
        }
        return Analyzer.self.builtins.Cont;
    }


    @NotNull
    @Override
    public String toString() {
        return "<Print:" + values + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(dest, v);
            visitNodeList(values, v);
        }
    }
}
