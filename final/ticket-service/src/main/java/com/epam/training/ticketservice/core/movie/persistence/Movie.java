package com.epam.training.ticketservice.core.movie.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Movies")
@Data
@NoArgsConstructor
public class Movie{
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String title;
    private String genre;
    private int length;

    public Movie(String title, String genre, int length) {
        this.title = title;
        this.genre = genre;
        this.length = length;
    }
}
