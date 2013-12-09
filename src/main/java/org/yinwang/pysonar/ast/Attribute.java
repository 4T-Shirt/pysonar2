package org.yinwang.pysonar.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.Binding;
import org.yinwang.pysonar.State;
import org.yinwang.pysonar.types.Type;
import org.yinwang.pysonar.types.UnionType;

import java.util.Set;

import static org.yinwang.pysonar.Binding.Kind.ATTRIBUTE;


public class Attribute extends Node {

    @NotNull
    public Node target;
    @NotNull
    public Name attr;


    public Attribute(@NotNull Node target, @NotNull Name attr, int start, int end) {
        super(start, end);
        this.target = target;
        this.attr = attr;
        addChildren(target, attr);
    }


    @Nullable
    public String getAttributeName() {
        return attr.id;
    }


    @NotNull
    public Name getAttr() {
        return attr;
    }


    public void setAttr(State s, @NotNull Type v) {
        Type targetType = resolveExpr(target, s);
        if (targetType.isUnionType()) {
            Set<Type> types = targetType.asUnionType().getTypes();
            for (Type tp : types) {
                setAttrType(tp, v);
            }
        } else {
            setAttrType(targetType, v);
        }
    }


    private void setAttrType(@NotNull Type targetType, @NotNull Type v) {
        if (targetType.isUnknownType()) {
            Analyzer.self.putProblem(this, "Can't set attribute for UnknownType");
            return;
        }
        // new attr, mark the type as "mutated"
        if (targetType.getTable().lookupAttr(attr.id) == null ||
                !targetType.getTable().lookupAttrType(attr.id).equals(v))
        {
            targetType.setMutated(true);
        }
        targetType.getTable().insert(attr.id, attr, v, ATTRIBUTE);
    }


    @NotNull
    @Override
    public Type resolve(State s) {
        Type targetType = resolveExpr(target, s);
        if (targetType.isUnionType()) {
            Set<Type> types = targetType.asUnionType().getTypes();
            Type retType = Analyzer.self.builtins.unknown;
            for (Type tt : types) {
                retType = UnionType.union(retType, getAttrType(tt));
            }
            return retType;
        } else {
            return getAttrType(targetType);
        }
    }


    private Type getAttrType(@NotNull Type targetType) {
        Binding b = targetType.getTable().lookupAttr(attr.id);
        if (b == null) {
            Analyzer.self.putProblem(attr, "attribute not found in type: " + targetType);
            Type t = Analyzer.self.builtins.unknown;
            t.getTable().setPath(targetType.getTable().extendPath(attr.id));
            return t;
        } else {
            Analyzer.self.putRef(attr, b);
            if (getParent() != null && getParent().isCall() &&
                    b.getType().isFuncType() && targetType.isInstanceType())
            {  // method call
                b.getType().asFuncType().setSelfType(targetType);
            }
            return b.getType();
        }
    }


    @NotNull
    @Override
    public String toString() {
        return "<Attribute:" + start + ":" + target + "." + getAttributeName() + ">";
    }


    @Override
    public void visit(@NotNull NodeVisitor v) {
        if (v.visit(this)) {
            visitNode(target, v);
            visitNode(attr, v);
        }
    }
}
