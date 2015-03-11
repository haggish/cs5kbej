package org.katastrofi.cs5k;

import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;

@Singleton
@Transactional
public class Service {

    private final CodeSets codeSets;


    @Inject
    public Service(CodeSets codeSets) {
        this.codeSets = codeSets;
    }


    public Set<CodeSet> allCodeSets() {
        return codeSets.all();
    }

    public Optional<CodeSet> codeSetWithName(String name) {
        return codeSets.withName(name);
    }

    public boolean addOrUpdate(CodeSet codeSet) {
        return codeSets.addOrUpdate(codeSet);
    }

    public boolean addOrUpdateCodeSetsCode(String codeSetName, Code code) {
        Optional<CodeSet> possibleCodeSet = codeSets.withName(codeSetName);
        if (possibleCodeSet.isPresent()) {
            CodeSet codeSet = possibleCodeSet.get();
            return codeSet.addOrUpdate(code);
        } else {
            throw new IllegalArgumentException("No code set by given name");
        }
    }

    public void clearCodeSets() {
        codeSets.removeAll();
    }

    public void removeCodeSetWithName(String name) {
        codeSets.removeWithName(name);
    }

    public void removeCodeSetsCode(String codeSetName, String codeName) {
        Optional<CodeSet> possibleCodeSet = codeSets.withName(codeSetName);
        if (possibleCodeSet.isPresent()) {
            possibleCodeSet.get().remove(codeName);
        }
    }
}
