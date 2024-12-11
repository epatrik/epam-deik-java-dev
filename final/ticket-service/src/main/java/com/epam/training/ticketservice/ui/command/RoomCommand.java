package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.account.AccountService;
import com.epam.training.ticketservice.core.account.model.AccountDto;
import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
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
public class RoomCommand {

    private final RoomService roomService;
    private final AccountService accountService;

    @ShellMethod(key = "create room", value = "Create a new room")
    @ShellMethodAvailability("isAvailable")
    public void createRoom(String name, int row, int col) {
        roomService.createRoom(name, row, col);
    }

    @ShellMethod(key = "update room", value = "Update a room")
    @ShellMethodAvailability("isAvailable")
    public void updateRoom(String name, int row, int col) {
        roomService.updateRoom(name, row, col);
    }

    @ShellMethod(key = "delete room", value = "Delete a room")
    @ShellMethodAvailability("isAvailable")
    public void deleteRoom(String name) {
        roomService.deleteRoom(name);
    }

    @ShellMethod(key = "list rooms", value = "List all movies")
    public String listRooms() {
        List<RoomDto> rooms = roomService.listRooms().orElseGet(List::of);
        if (rooms.isEmpty()) {
            return "There are no rooms at the moment";
        }
        return rooms.stream()
                .map(roomDto -> String.format("Room %s with %d seats, %d rows and %d columns",
                        roomDto.roomName(), roomDto.seatRowNumber() * roomDto.seatColumnNumber(),
                        roomDto.seatRowNumber(), roomDto.seatColumnNumber()))
                .collect(Collectors.joining("\n"));
    }

    private Availability isAvailable() {
        Optional<AccountDto> user = accountService.describe();
        return user.isPresent() && user.get().role() == Account.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
