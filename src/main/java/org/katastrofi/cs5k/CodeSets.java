package org.katastrofi.cs5k;

import java.util.Set;

public interface CodeSets {

    Set<CodeSet> all();

    CodeSet withName(String name);

    void removeAll();

    void removeWithName(String name);

    boolean include(String name);

    void addOrUpdate(CodeSet codeSet);

}
