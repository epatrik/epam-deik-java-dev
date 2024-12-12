package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.account.AccountService;
import com.epam.training.ticketservice.core.account.model.AccountDto;
import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final AccountService accountService;

    @ShellMethod(key = "create screening", value = "Create a new screening")
    @ShellMethodAvailability("isAvailable")
    public String createScreening(String movieTitle, String roomName, String start) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return screeningService.createScreening(movieTitle, roomName, startTime);
        } catch (DateTimeParseException e) {
            return "Invalid date format";
        }
    }

    @ShellMethod(key = "delete screening", value = "Delete a screening")
    @ShellMethodAvailability("isAvailable")
    public void deleteScreening(String movieTitle, String roomName, String start) {
        try {
            LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            screeningService.deleteScreening(movieTitle, roomName, startTime);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format");
        }
    }

    @ShellMethod(key = "list screenings", value = "List all screenings")
    public String listScreenings() {
        List<ScreeningDto> screenings = screeningService.listScreenings().orElseGet(List::of);
        if (screenings.isEmpty()) {
            return "There are no screenings";
        }
        return screenings.stream()
                .map(screeningDto -> String.format("%s (%s, %d minutes), screened in room %s, at %s",
                        screeningDto.movieTitle(),
                        movieService.getMovieByTitle(screeningDto.movieTitle()).get().genre(),
                        movieService.getMovieByTitle(screeningDto.movieTitle()).get().length(),
                        screeningDto.roomName(),
                        screeningDto.startDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().role() == Account.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
