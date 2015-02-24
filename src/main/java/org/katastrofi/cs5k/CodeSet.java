package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Maps.uniqueIndex;

@JsonDeserialize(converter = Protocols.ToCodeSet.class)
@JsonSerialize(converter = Protocols.FromCodeSet.class)
final class CodeSet extends NamedObject {

    private final Map<String, Code> codes;

    CodeSet(String name, String description, Set<Code> codes) {
        super(name, description);
        this.codes = uniqueIndex(codes, Code::name);
    }

    Set<Code> codes() {
        return copyOf(codes.values());
    }


    boolean hasCodeNamed(String codeSetName) {
        return codes.containsKey(codeSetName);
    }
}
