package org.katastrofi.cs5k;

import com.google.common.collect.Range;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class CodeSetTest {

    private CodeSet testedCodeSet;

    private Code code1, code2, existingCode, updatedExistingCode, newCode;


    @Before
    public void init() {
        code1 = new Code("C01", "Code 1", emptyMap());
        Map<Range<LocalDateTime>, String> map = newHashMap();
        map.put(Range.closedOpen(
                        LocalDateTime.of(2015, 1, 1, 0, 0),
                        LocalDateTime.of(2017, 6, 7, 0, 0)),
                "a");
        code2 = new Code("C02", "Code 2", map);
        existingCode = code2;
        updatedExistingCode = new Code(existingCode.name(), "New desc",
                existingCode.codevalues());
        newCode = new Code("C03", "Code 3", emptyMap());
        testedCodeSet =
                new CodeSet("CS01", "Code Set 1", newHashSet(code1, code2));
    }


    @Test
    public void addingCodeAddsItToCodes() {
        testedCodeSet.addOrUpdate(newCode);

        assertThat(testedCodeSet.codes(),
                containsInAnyOrder(code1, code2, newCode));
    }

    @Test
    public void updatingCodeReplacesExistingWithGiven() {
        testedCodeSet.addOrUpdate(updatedExistingCode);

        assertThat(testedCodeSet.codes(),
                containsInAnyOrder(code1, existingCode));
        assertThat(testedCodeSet.code(existingCode.name()).description(),
                is(updatedExistingCode.description()));
    }

    @Test
    public void updatingCodeWithChangedValuesReplacesTheOldValues() {
        Map<Range<LocalDateTime>, String> updatedValues = newHashMap();
        Range<LocalDateTime> oldEffectivity =
                existingCode.codevalues().keySet().iterator().next();
        updatedValues.put(
                Range.closedOpen(oldEffectivity.lowerEndpoint(), now()), "b");
        updatedExistingCode = new Code(existingCode.name(),
                existingCode.description(), updatedValues);

        testedCodeSet.addOrUpdate(updatedExistingCode);

        assertThat(testedCodeSet.codes(),
                containsInAnyOrder(code1, existingCode));
        assertThat(testedCodeSet.code(existingCode.name()).codevalues(),
                is(updatedExistingCode.codevalues()));
    }

    @Test
    public void addingCodeReturnsFalse() {
        assertThat(testedCodeSet.addOrUpdate(newCode), is(false));
    }

    @Test
    public void updatingCodeReturnsTrue() {
        assertThat(testedCodeSet.addOrUpdate(updatedExistingCode), is(true));
    }

    @Test
    public void removingExistingCodeRemovesCode() {
        testedCodeSet.remove(existingCode.name());

        assertThat(testedCodeSet.codes(), not(contains(existingCode)));
    }

    @Test
    public void removingNonExistingCodeLeavesCodesUnchanged() {
        testedCodeSet.remove("nonExistingCode");

        assertThat(testedCodeSet.codes(), containsInAnyOrder(code1, code2));
    }
}
