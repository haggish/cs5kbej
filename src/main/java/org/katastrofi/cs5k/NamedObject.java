package org.katastrofi.cs5k;

import static org.katastrofi.cs5k.Checks.nonEmpty;

abstract class NamedObject {

    private final String name;

    private final String description;


    NamedObject(String name, String description) {
        this.name = nonEmpty(name);
        this.description = description;
    }


    String name() {
        return name;
    }

    String description() {
        return description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedObject that = (NamedObject) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "NamedObject{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
