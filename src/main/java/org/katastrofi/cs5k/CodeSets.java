package org.katastrofi.cs5k;

import java.util.Optional;
import java.util.Set;

/**
 * Repository of code sets.
 */
interface CodeSets {

    /**
     * @return all the code sets
     */
    Set<CodeSet> all();

    /**
     * @param name name for requested code set
     * @return code set with given name, if any
     */
    Optional<CodeSet> withName(String name);

    /**
     * Removes all the code sets.
     */
    void removeAll();

    /**
     * Removes code set of given name.
     *
     * @param name name for code set to be removed
     */
    void removeWithName(String name);

    /**
     * Adds new or updates existing code set
     *
     * @param codeSet added or updated code set
     * @return whether the code set already existed
     */
    boolean addOrUpdate(CodeSet codeSet);

}
