package ru.iu3.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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


    @Column(name = "year")
    public long year;

    @ManyToOne()
    @JoinColumn(name="artistid")
    public Artists artist;

    @ManyToOne()
    @JoinColumn(name="museumid")
    public Museum museum;

}
