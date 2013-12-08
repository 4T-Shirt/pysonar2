package org.yinwang.pysonar.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yinwang.pysonar.Analyzer;
import org.yinwang.pysonar.State;
import org.yinwang.pysonar._;


public class ModuleType extends Type {

    @Nullable
    private String file;
    @NotNull
    private String name;
    @Nullable
    private String qname;


    public ModuleType(@NotNull String name, @Nullable String file, @NotNull State parent) {
        this.name = name;
        this.file = file;  // null for builtin modules
        if (file != null) {
            // This will return null iff specified file is not prefixed by
            // any path in the module search path -- i.e., the caller asked
            // the analyzer to load a file not in the search path.
            qname = _.moduleQname(file);
        }
        if (qname == null) {
            qname = name;
        }
        setTable(new State(parent, State.StateType.MODULE));
        getTable().setPath(qname);
        getTable().setType(this);

        // null during bootstrapping of built-in types
        if (Analyzer.self.builtins != null) {
            getTable().addSuper(Analyzer.self.builtins.BaseModule.getTable());
        }
    }


    public void setFile(String file) {
        this.file = file;
    }


    @Nullable
    public String getFile() {
        return file;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    @Nullable
    public String getQname() {
        return qname;
    }


    @Override
    public int hashCode() {
        return "ModuleType".hashCode();
    }


    @Override
    public boolean equals(Object other) {
        if (other instanceof ModuleType) {
            ModuleType co = (ModuleType) other;
            if (file != null) {
                return file.equals(co.file);
            }
        }
        return this == other;
    }


    @Override
    protected String printType(CyclicTypeRecorder ctr) {
        return getName();
    }
}
