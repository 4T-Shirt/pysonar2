package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.SuperState;
import org.yinwang.pysonar.types.Type;
import org.yinwang.pysonar.types.UnionType;

import java.util.List;


public class Try extends Node {

    public List<ExceptHandler> handlers;
    public Block body;
    public Block orelse;
    public Block finalbody;

    public Try(List<ExceptHandler> handlers, Block body, Block orelse, Block finalbody,
                     int start, int end)
    {
        super(start, end);
        this.handlers = handlers;
        this.body = body;
        this.orelse = orelse;
        this.finalbody = finalbody;
        addChildren(handlers);
        addChildren(body, orelse);
    }


    @NotNull
    @Override
    public SuperState transform(SuperState s) {
        Type tp1 = Analyzer.self.builtins.unknown;
        Type tp2 = Analyzer.self.builtins.unknown;
        Type tph = Analyzer.self.builtins.unknown;
        Type tpFinal = Analyzer.self.builtins.unknown;

        for (ExceptHandler h : handlers) {
            tph = UnionType.union(tph, transformExpr(h, s));
        }

        if (body != null) {
            tp1 = transformExpr(body, s);
        }

        if (orelse != null) {
            tp2 = transformExpr(orelse, s);
        }

        if (finalbody != null) {
            tpFinal = transformExpr(finalbody, s);
        }


        return new UnionType(tp1, tp2, tph, tpFinal);
    }


    @NotNull
    @Override
    public String toString() {
        return "<Try:" + handlers + ":" + body + ":" + orelse + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNodeList(handlers, v);
            visitNode(body, v);
            visitNode(orelse, v);
        }
    }
}
