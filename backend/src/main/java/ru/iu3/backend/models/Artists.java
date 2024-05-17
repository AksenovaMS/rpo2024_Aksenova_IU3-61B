package ru.iu3.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
@Access(AccessType.FIELD)

public class Artists {

        public Artists() { }
        public Artists(Long id) {
            this.id = id;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", updatable = false, nullable = false)
        public long id;

        @Column(name = "name", nullable = false, unique = true)
        public String name;

        @Column(name = "century", nullable = false)
        public String century;

        @ManyToOne()
        @JoinColumn(name = "countryid")
        public Country country;

        @JsonIgnore
        @OneToMany(mappedBy = "artist")
        public List<Painting> paintings = new ArrayList<Painting>();

}
