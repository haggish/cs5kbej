package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Sets.newHashSet;

@JsonDeserialize(converter = Protocols.ToCode.class)
@JsonSerialize(converter = Protocols.FromCode.class)
final class Code extends NamedObject {

    private final Set<String> values;


    Code(String name, String description, Set<String> values) {
        super(name, description);
        this.values = newHashSet(values);
    }


    Set<String> values() {
        return copyOf(values);
    }
}
