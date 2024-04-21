package ru.iu3.backend.models;

import javax.persistence.*;

@Entity
@Table(name = "paintings")
@Access(AccessType.FIELD)

public class Painting {

    public Painting() { }
    public Painting(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "artistid")
    public long artistId;

    @Column(name = "museumid")
    public long museumId;

    @Column(name = "year")
    public long year;

}
