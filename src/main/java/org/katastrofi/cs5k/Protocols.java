package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

final class Protocols {

    private Protocols() {
    }

    static abstract class TempNamedObject {
        public String name;
        public String description;
    }

    static class TempCodeSet extends TempNamedObject {
        public Set<Code> codes;
    }

    static class TempCode extends TempNamedObject {
        public Set<TempValue> values;
    }

    static class TempValue {
        private static TempValue from(Effectivity effectivity, String value) {
            TempValue tv = new TempValue();
            tv.effectivityString = effectivity.toString();
            tv.value = value;
            return tv;
        }

        public String effectivityString;
        public String value;
    }

    static final class ToCodeSet
            extends StdConverter<TempCodeSet, CodeSet> {
        @Override
        public CodeSet convert(TempCodeSet value) {
            return new CodeSet(value.name, value.description, value.codes);
        }
    }

    static final class FromCodeSet
            extends StdConverter<CodeSet, TempCodeSet> {
        @Override
        public TempCodeSet convert(CodeSet value) {
            TempCodeSet temp = new TempCodeSet();
            temp.name = value.name();
            temp.description = value.description();
            temp.codes = value.codes();
            return temp;
        }
    }

    static final class ToCode
            extends StdConverter<TempCode, Code> {
        @Override
        public Code convert(TempCode value) {
            return new Code(value.name, value.description, value.values.stream()
                    .collect(toMap(
                            e -> Effectivity.from(e.effectivityString)
                                    .timeRange(),
                            e -> e.value)));
        }
    }

    static final class FromCode extends StdConverter<Code, TempCode> {
        @Override
        public TempCode convert(Code value) {
            TempCode temp = new TempCode();
            temp.name = value.name();
            temp.description = value.description();
            temp.values = value.codevalues().entrySet().stream().map(
                    e -> TempValue.from(
                            new Effectivity(e.getKey()), e.getValue()))
                    .collect(toSet());
            return temp;
        }
    }
}
