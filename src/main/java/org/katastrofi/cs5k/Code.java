package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Range;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.newHashSet;
import static javax.persistence.FetchType.EAGER;

@Entity
@JsonDeserialize(converter = Protocols.ToCode.class)
@JsonSerialize(converter = Protocols.FromCode.class)
final class Code extends NamedObject implements Serializable {

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "codevalues",
            joinColumns = @JoinColumn(name = "cvalue_id"))
    @Column(name = "codevalue")
    @NonFinalForHibernate
    private Set<String> codevalues;

    private Effectivity effectivity;

    @ForHibernateOnly
    Code() {
        super();
        effectivity = new Effectivity(Range.all());
    }

    Code(String name, String description, Set<String> codevalues) {
        super(name, description);
        effectivity = new Effectivity(Range.all());
        this.codevalues = newHashSet(codevalues);
    }


    Set<String> codevalues() {
        return copyOf(codevalues);
    }


    void mergeWith(Code updatedCode) {
        super.mergeWith(updatedCode);
        codevalues.removeAll(
                difference(codevalues(), updatedCode.codevalues()));
        codevalues.addAll(difference(updatedCode.codevalues(), codevalues()));
    }
}
