package org.katastrofi.cs5k;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.Set;

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
        public Set<String> values;
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
            return new Code(value.name, value.description, value.values);
        }
    }

    static final class FromCode extends StdConverter<Code, TempCode> {
        @Override
        public TempCode convert(Code value) {
            TempCode temp = new TempCode();
            temp.name = value.name();
            temp.description = value.description();
            temp.values = value.codevalues();
            return temp;
        }
    }
}
