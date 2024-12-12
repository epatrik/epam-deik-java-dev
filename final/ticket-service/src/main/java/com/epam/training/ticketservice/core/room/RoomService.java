package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    void createRoom(String roomName, int seatRowNumber, int seatColumnNumber);

    void updateRoom(String roomName, int seatRowNumber, int seatColumnNumber);

    void deleteRoom(String roomName);

    Optional<List<RoomDto>> listRooms();

    Optional<RoomDto> findByRoomName(String roomName);
}
