package org.katastrofi.cs5k;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Maps.newHashMap;

public class TransientCodeSets implements CodeSets {

    private final Map<String, CodeSet> codeSetsByNames = newHashMap();


    @Override
    public Set<CodeSet> all() {
        return copyOf(codeSetsByNames.values());
    }

    @Override
    public CodeSet withName(String name) {
        return codeSetsByNames.get(name);
    }

    @Override
    public void removeAll() {
        codeSetsByNames.clear();
    }

    @Override
    public void removeWithName(String name) {
        codeSetsByNames.remove(name);
    }

    @Override
    public boolean include(String name) {
        return codeSetsByNames.containsKey(name);
    }

    @Override
    public void addOrUpdate(CodeSet codeSet) {
        codeSetsByNames.put(codeSet.name(), codeSet);
    }
}
