package org.katastrofi.cs5k;

import static com.google.common.base.Preconditions.checkNotNull;

final class Checks {

    private Checks() {
    }

    static String nonEmpty(String string) {
        if (checkNotNull(string, "Value may not be null").isEmpty()) {
            throw new IllegalArgumentException("Value may not be empty");
        }
        ;
        return string;
    }
}
