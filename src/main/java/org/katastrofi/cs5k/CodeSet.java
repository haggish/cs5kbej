package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@JsonDeserialize(converter = Protocols.ToCodeSet.class)
@JsonSerialize(converter = Protocols.FromCodeSet.class)
final class CodeSet extends NamedObject implements Serializable {

    @OneToMany(orphanRemoval = true, cascade = ALL, fetch = EAGER)
    @JoinColumn(name="csid")
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
        return codes.put(code.name(), code) != null;
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
}
