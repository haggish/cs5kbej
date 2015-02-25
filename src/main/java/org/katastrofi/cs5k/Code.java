package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.OnDelete;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Sets.newHashSet;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@JsonDeserialize(converter = Protocols.ToCode.class)
@JsonSerialize(converter = Protocols.FromCode.class)
final class Code extends NamedObject implements Serializable {

    @ElementCollection
    @CollectionTable(name = "values",
            joinColumns = @JoinColumn(name = "value_id"))
    @Column(name = "value")
    @NonFinalForHibernate
    private Set<String> values;


    @ForHibernateOnly
    Code() {
        super();
    }

    Code(String name, String description, Set<String> values) {
        super(name, description);
        this.values = newHashSet(values);
    }


    Set<String> values() {
        return copyOf(values);
    }
}
