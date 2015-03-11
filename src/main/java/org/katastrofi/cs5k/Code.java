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
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Sets.difference;
import static java.util.stream.Collectors.toMap;
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
    private Map<Effectivity, String> codevalues;

    private Effectivity effectivity;

    @ForHibernateOnly
    Code() {
        super();
        effectivity = new Effectivity(Range.all());
    }

    Code(String name, String description,
         Map<Range<LocalDateTime>, String> codevalues) {
        super(name, description);
        effectivity = new Effectivity(Range.all());
        this.codevalues = codevalues.entrySet().stream().collect(toMap(
                e -> new Effectivity(e.getKey()),
                Map.Entry::getValue));
    }


    Map<Range<LocalDateTime>, String> codevalues() {
        return codevalues.entrySet().stream().collect(toMap(
                e -> e.getKey().timeRange(),
                Map.Entry::getValue));
    }

    void mergeWith(Code updatedCode) {
        super.mergeWith(updatedCode);
        difference(codevalues.keySet(), updatedCode.codevalues.keySet())
                .stream().forEach(codevalues::remove);
        difference(updatedCode.codevalues.entrySet(), codevalues.entrySet())
                .stream().forEach(
                e -> codevalues.put(e.getKey(), e.getValue()));
    }
}
