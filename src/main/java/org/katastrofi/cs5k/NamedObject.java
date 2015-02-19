package org.katastrofi.cs5k;

abstract class NamedObject {

    private final String name;

    private final String description;


    NamedObject(String name, String description) {
        this.name = name;
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

        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
