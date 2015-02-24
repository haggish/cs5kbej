package org.katastrofi.cs5k;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;
import static org.katastrofi.cs5k.Checks.nonEmpty;

public class ChecksTest {

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void nonEmptyStringIsGivenString() {
        assertThat(nonEmpty("string", "name"), is("string"));
    }

    @Test
    public void nullStringIsNPE() {
        expectedException.expect(NullPointerException.class);
        nonEmpty(null, "name");
    }

    @Test
    public void nullStringNPEMessageIsThatGivenNameMayNotBeNull() {
        expectedException.expectMessage("name may not be null");
        nonEmpty(null, "name");
    }

    @Test
    public void emptyStringIsIAE() {
        expectedException.expect(IllegalArgumentException.class);
        nonEmpty("", "name");
    }

    @Test
    public void emptyStringIAEMessageIsThatGivenNameMayNotBeEmpty() {
        expectedException.expectMessage("name may not be empty");
        nonEmpty("", "name");
    }
}
