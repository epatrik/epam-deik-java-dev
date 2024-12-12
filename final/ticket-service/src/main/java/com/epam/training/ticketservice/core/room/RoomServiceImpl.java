package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public void createRoom(String name, int row, int col) {
        Room room = new Room(name, row, col);
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(String name, int row, int col) {
        Optional<Room> room = roomRepository.findByRoomName(name);
        if (room.isPresent()) {
            room.get().setSeatRowNumber(row);
            room.get().setSeatColNumber(col);
            roomRepository.save(room.get());
        }
    }

    @Override
    public void deleteRoom(String name) {
        Optional<Room> room = roomRepository.findByRoomName(name);
        room.ifPresent(roomRepository::delete);
    }

    @Override
    public Optional<List<RoomDto>> listRooms() {
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rooms.stream()
                .map(room -> new RoomDto(room.getRoomName(), room.getSeatRowNumber(), room.getSeatColNumber()))
                .collect(Collectors.toList()));
    }
}
