package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.copyOf;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@JsonDeserialize(converter = Protocols.ToCodeSet.class)
@JsonSerialize(converter = Protocols.FromCodeSet.class)
final class CodeSet extends NamedObject {

    private final Map<String, Code> codes;

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
}
