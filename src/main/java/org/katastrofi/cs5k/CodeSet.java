package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Sets.difference;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@JsonDeserialize(converter = Protocols.ToCodeSet.class)
@JsonSerialize(converter = Protocols.FromCodeSet.class)
final class CodeSet extends NamedObject implements Serializable {

    @OneToMany(orphanRemoval = true, cascade = ALL, fetch = EAGER)
    @JoinColumn(name = "csid")
    @NonFinalForHibernate
    private Map<String, Code> codes;


    @ForHibernateOnly
    CodeSet() {
        super();
    }

    CodeSet(String name, String description, Set<Code> codes) {
        super(name, description);
        this.codes = codes.stream().collect(toMap(Code::name, identity()));
    }


    Set<Code> codes() {
        return copyOf(codes.values());
    }


    boolean addOrUpdate(Code code) {
        if (codes.containsKey(code.name())) {
            code(code.name()).mergeWith(code);
            return true;
        } else {
            codes.put(code.name(), code);
            return false;
        }
    }

    void remove(String codeName) {
        codes.remove(codeName);
    }

    Code code(String name) {
        return codes.get(name);
    }

    @Override
    public String toString() {
        return "CodeSet{" +
                super.toString() +
                "codes=" + codes +
                '}';
    }

    public CodeSet mergeWith(CodeSet updatedCodeSet) {
        super.mergeWith(updatedCodeSet);
        removeCodesNotEqualToAnyIn(updatedCodeSet);
        addCodesNotEqualToAnyInOldCodeSetFrom(updatedCodeSet);
        return this;
    }


    private void addCodesNotEqualToAnyInOldCodeSetFrom(CodeSet updatedCodeSet) {
        difference(updatedCodeSet.codes(), codes()).stream()
                .forEach(c -> codes.put(c.name(), c));
    }

    private void removeCodesNotEqualToAnyIn(CodeSet updatedCodeSet) {
        difference(codes(), updatedCodeSet.codes()).stream()
                .forEach(c -> codes.remove(c.name()));
    }
}
