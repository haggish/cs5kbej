package org.katastrofi.cs5k;

import org.hibernate.annotations.NaturalId;

import javax.persistence.MappedSuperclass;

import static org.katastrofi.cs5k.Checks.nonEmpty;

@MappedSuperclass
abstract class NamedObject extends IdentifiableObject {

    @NaturalId
    private String name;

    private String description;


    @ForHibernateOnly
    NamedObject() {
        super();
    }

    NamedObject(String name, String description) {
        super();
        this.name = nonEmpty(name, "name");
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
