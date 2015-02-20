package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@JsonDeserialize(converter = RESTEndpoint.ToCode.class)
final class Code extends NamedObject {

    private final Set<String> values;

    Code(String name, String description, Set<String> values) {
        super(name, description);
        this.values = newHashSet(values);
    }
}
