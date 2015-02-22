package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Sets.newHashSet;

@JsonDeserialize(converter = RESTEndpoint.ToCodeSet.class)
@JsonSerialize(converter = RESTEndpoint.FromCodeSet.class)
final class CodeSet extends NamedObject {

    private final Set<Code> codes;

    CodeSet(String name, String description, Set<Code> codes) {
        super(name, description);
        this.codes = newHashSet(codes);
    }

    Set<Code> codes() {
        return copyOf(codes);
    }
}
