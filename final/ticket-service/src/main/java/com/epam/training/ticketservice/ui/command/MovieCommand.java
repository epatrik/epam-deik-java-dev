package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.account.AccountService;
import com.epam.training.ticketservice.core.account.model.AccountDto;
import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
@AllArgsConstructor
public class MovieCommand {

    private final MovieService movieService;
    private final AccountService accountService;

    @ShellMethod(key = "create movie", value = "Create a new movie")
    @ShellMethodAvailability("isAvailable")
    public void createMovie(String title, String genre, int length) {
        movieService.createMovie(title, genre, length);
    }

    @ShellMethod(key = "update movie", value = "Update a movie")
    @ShellMethodAvailability("isAvailable")
    public void updateMovie(String title, String genre, int length) {
        movieService.updateMovie(title, genre, length);
    }

    @ShellMethod(key = "delete movie", value = "Delete a movie")
    @ShellMethodAvailability("isAvailable")
    public void deleteMovie(String title) {
        movieService.deleteMovie(title);
    }

    @ShellMethod(key = "list movies", value = "List all movies")
    public String listMovies() {
        List<MovieDto> movies = movieService.listMovies().orElseGet(List::of);
        if (movies.isEmpty()) {
            return "There are no movies at the moment";
        }
        return movies.stream()
                .map(movie -> String.format("%s (%s, %d minutes)", movie.title(), movie.genre(), movie.length()))
                .collect(Collectors.joining("\n"));
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().role() == Account.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
