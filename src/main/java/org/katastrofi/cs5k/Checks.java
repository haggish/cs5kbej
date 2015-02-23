package org.katastrofi.cs5k;

import static com.google.common.base.Preconditions.checkNotNull;

final class Checks {

    private Checks() {
    }

    static String nonEmpty(String string, String name) {
        if (checkNotNull(string, name + " may not be null").isEmpty()) {
            throw new IllegalArgumentException(name + " may not be empty");
        }
        return string;
    }
}
