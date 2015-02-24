package org.katastrofi.cs5k;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Maps.newHashMap;

final class TransientCodeSets implements CodeSets {

    private final Map<String, CodeSet> codeSetsByNames = newHashMap();


    @Override
    public Set<CodeSet> all() {
        return copyOf(codeSetsByNames.values());
    }

    @Override
    public Optional<CodeSet> withName(String name) {
        return Optional.ofNullable(codeSetsByNames.get(name));
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
    public boolean addOrUpdate(CodeSet codeSet) {
        return codeSetsByNames.put(codeSet.name(), codeSet) != null;
    }
}
