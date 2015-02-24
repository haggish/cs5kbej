package org.katastrofi.cs5k;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

public class TransientCodeSetsTest {

    private TransientCodeSets testedTransientCodeSets;

    private CodeSet cs1, cs2, newCodeSet, updatedCS1;


    @Before
    public void init() {
        testedTransientCodeSets = new TransientCodeSets();
        cs1 = new CodeSet("CS01", "desc", newHashSet());
        cs2 = new CodeSet("CS02", "another desc", newHashSet());
        newCodeSet = new CodeSet("CS03", "new desc", newHashSet());
        updatedCS1 = new CodeSet(cs1.name(), "updated desc", cs1.codes());
        testedTransientCodeSets.addOrUpdate(cs1);
        testedTransientCodeSets.addOrUpdate(cs2);
    }


    @Test
    public void allReturnsAllCodeSetValues() {
        assertThat(testedTransientCodeSets.all(), containsInAnyOrder(cs1, cs2));
    }

    @Test
    public void queryingAnExistingCodeSetReturnsOptionalOfWhatIsInValues() {
        assertThat(testedTransientCodeSets.withName(cs1.name()),
                is(Optional.of(cs1)));
    }

    @Test
    public void queryingANonExistingCodeSetReturnsEmptyOptional() {
        assertThat(testedTransientCodeSets.withName("cs3"), is(empty()));
    }

    @Test
    public void removingAllClearsCodeSets() {
        testedTransientCodeSets.removeAll();

        assertThat(testedTransientCodeSets.all().isEmpty(), is(true));
    }

    @Test
    public void removingCodeSetByNameRemovesItFromValues() {
        testedTransientCodeSets.removeWithName(cs1.name());

        assertThat(testedTransientCodeSets.withName(cs1.name()), is(empty()));
    }

    @Test
    public void addingCodeSetReturnsFalse() {
        assertThat(testedTransientCodeSets.addOrUpdate(newCodeSet), is(false));
    }

    @Test
    public void addingCodeSetAddsItToValues() {
        testedTransientCodeSets.addOrUpdate(newCodeSet);

        assertThat(testedTransientCodeSets.withName(newCodeSet.name()),
                is(Optional.of(newCodeSet)));
    }

    @Test
    public void updatingCodeSetReturnsTrue() {
        assertThat(testedTransientCodeSets.addOrUpdate(updatedCS1), is(true));
    }

    @Test
    public void updatingCodeSetUpdatesItsValues() {
        testedTransientCodeSets.addOrUpdate(updatedCS1);

        assertThat(testedTransientCodeSets
                .withName(cs1.name()).get().description(),
                is(updatedCS1.description()));
    }
}
