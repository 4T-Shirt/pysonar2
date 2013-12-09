package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.yinwang.pysonar.SuperState;
import org.yinwang.pysonar.types.ListType;

import java.util.List;


public class Set extends Sequence {

    public Set(List<Node> elts, int start, int end) {
        super(elts, start, end);
    }


    @NotNull
    @Override
    public SuperState transform(SuperState s) {
        if (elts.size() == 0) {
            return new ListType();
        }

        ListType listType = null;
        for (Node elt : elts) {
            if (listType == null) {
                listType = new ListType(transformExpr(elt, s));
            } else {
                listType.add(transformExpr(elt, s));
            }
        }

        return listType;
    }


    @NotNull
    @Override
    public String toString() {
        return "<List:" + start + ":" + elts + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNodeList(elts, v);
        }
    }
}
