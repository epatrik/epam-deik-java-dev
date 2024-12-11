package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    @Override
    public void createMovie(String title, String genre, int length) {
        Movie movie = new Movie(title, genre, length);
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(String title, String genre, int length) {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        if (movie.isPresent()) {
            movie.get().setGenre(genre);
            movie.get().setLength(length);
            movieRepository.save(movie.get());
        }
    }

    @Override
    public void deleteMovie(String title) {
        Optional<Movie> movie = movieRepository.findByTitle(title);
        movie.ifPresent(movieRepository::delete);
    }

    @Override
    public Optional<List<MovieDto>> listMovies() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(movies.stream()
                .map(movie -> new MovieDto(movie.getTitle(), movie.getGenre(), movie.getLength()))
                .collect(Collectors.toList()));
    }
}
