package org.katastrofi.cs5k;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class NamedObjectTest {

    @Test
    public void equalNamedObjectsHaveEqualNames() {
        NamedObject a = new TestNamedObject("name", "desc");
        NamedObject b = new TestNamedObject("name", "other desc");

        assertThat(a, is(b));
    }

    @Test
    public void nonEqualNamedObjectsHaveNonEqualNames() {
        NamedObject a = new TestNamedObject("name", "desc");
        NamedObject b = new TestNamedObject("other name", "other desc");

        assertThat(a, is(not(b)));
    }

    @Test(expected = NullPointerException.class)
    public void namedObjectWithoutNameIsNPE() {
        new TestNamedObject(null, "desc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void namedObjectWithEmptyNameIsIAE() {
        new TestNamedObject("", "desc");
    }


    private static class TestNamedObject extends NamedObject {
        TestNamedObject(String name, String description) {
            super(name, description);
        }
    }
}
