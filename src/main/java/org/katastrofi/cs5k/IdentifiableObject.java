package org.katastrofi.cs5k;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdentifiableObject {

    @Id
    @GeneratedValue
    private Integer id;

}
